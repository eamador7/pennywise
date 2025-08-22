package com.example.expense_tracker

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.pie.PieChart
import com.patrykandpatrick.vico.core.model.PieModel
import com.patrykandpatrick.vico.core.model.slice

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SummaryScreen(viewModel: SummaryViewModel) {
    val filteredExpenses by viewModel.filteredExpenses.collectAsState()
    val chartData by viewModel.chartData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Filter Section
        FilterSection(viewModel = viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Graph
        SummaryChart(chartData = chartData)

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
fun SummaryChart(chartData: Map<String, Double>) {
    if (chartData.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text("Please select a single currency to see a summary chart.", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        val chartColors = listOf(
            Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
            Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
            Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39)
        )

        val model = PieModel(
            slices = chartData.entries.mapIndexed { index, entry ->
                slice(
                    label = entry.key,
                    value = entry.value.toFloat(),
                    color = chartColors[index % chartColors.size]
                )
            }
        )

        PieChart(
            pieModel = model,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
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
