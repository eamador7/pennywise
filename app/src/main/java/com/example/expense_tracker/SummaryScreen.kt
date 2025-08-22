package com.example.expense_tracker

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.google.accompanist.flowlayout.FlowRow

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

@Composable
fun FilterSection() {
    val filters = listOf("Day", "Week", "Month", "Year", "Category", "Currency")
    Text("Filters", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
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
