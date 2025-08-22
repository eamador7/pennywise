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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SummaryScreen(viewModel: SummaryViewModel) {
    val recentExpenses by viewModel.recentExpenses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Filter Section
        FilterSection()

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Graph Placeholder
        GraphPlaceholder()

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Recent Expenses Section
        RecentExpensesSection(expenses = recentExpenses)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSection() {
    val filters = listOf("Day", "Week", "Month", "Year", "Category", "Currency")
    Text("Filters", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            Button(onClick = { /* TODO */ }) {
                Text(filter)
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
