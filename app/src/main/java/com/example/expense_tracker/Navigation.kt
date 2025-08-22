package com.example.expense_tracker

sealed class Screen(val route: String) {
    object AddExpense : Screen("add_expense")
    object Summary : Screen("summary")
    object AllExpenses : Screen("all_expenses")
}
