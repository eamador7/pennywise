package com.example.expense_tracker

import androidx.compose.foundation.border
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SummaryScreen(viewModel: SummaryViewModel) {
    val filteredExpenses by viewModel.filteredExpenses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Filter Section
        FilterSection(viewModel = viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Graph Placeholder
        GraphPlaceholder()

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Recent Expenses Section
        RecentExpensesSection(expenses = filteredExpenses)
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(viewModel: SummaryViewModel) {
    val timeFilters = listOf("Day", "Week", "Month", "Year")
    val categories by viewModel.uniqueCategories.collectAsState()
    val currencies by viewModel.uniqueCurrencies.collectAsState()

    Text("Filters", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        timeFilters.forEach { filter ->
            Button(onClick = {
                viewModel.setDateFilter(TimeFilter.valueOf(filter.uppercase()))
            }) {
                Text(filter)
            }
        }

        // Category Dropdown
        FilterDropdown(
            options = listOf("All") + categories,
            label = "Category",
            onItemSelected = { viewModel.setCategory(it) }
        )

        // Currency Dropdown
        FilterDropdown(
            options = listOf("All") + currencies,
            label = "Currency",
            onItemSelected = { viewModel.setCurrency(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(options: List<String>, label: String, onItemSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        onItemSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun GraphPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text("Graph will be here", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun RecentExpensesSection(expenses: List<Expense>) {
    Text("Recent Expenses", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    if (expenses.isEmpty()) {
        Text("No recent expenses to show.")
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(expenses) { expense ->
                ExpenseListItem(expense = expense)
            }
        }
    }
}
