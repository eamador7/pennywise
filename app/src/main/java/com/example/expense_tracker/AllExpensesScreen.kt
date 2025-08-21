package com.example.expense_tracker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AllExpensesScreen(viewModel: AllExpensesViewModel) {
    val lazyPagingItems = viewModel.expenses.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index -> lazyPagingItems.peek(index)?.id ?: index }
                ) { index ->
                    val expense = lazyPagingItems[index]
                    if (expense != null) {
                        ExpenseListItem(expense = expense)
                    }
                }

                lazyPagingItems.apply {
                    when {
                        loadState.append is LoadState.Loading -> {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val e = lazyPagingItems.loadState.refresh as LoadState.Error
                            item {
                                Text(
                                    text = "Error: ${e.error.localizedMessage}",
                                    modifier = Modifier.fillParentMaxSize(),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            val e = lazyPagingItems.loadState.append as LoadState.Error
                            item {
                                Text(
                                    text = "Error: ${e.error.localizedMessage}",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseListItem(expense: Expense) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${expense.amount} ${expense.currency}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Category: ${expense.category}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            Text(
                text = "Date: ${sdf.format(Date(expense.date))}",
                style = MaterialTheme.typography.bodySmall
            )
            if (expense.description != null && expense.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Description: ${expense.description}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
