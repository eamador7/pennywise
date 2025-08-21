package com.example.expense_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val application = application as ExpenseApplication
            val viewModel: AddExpenseViewModel by viewModels {
                AddExpenseViewModelFactory(application.database.expenseDao())
            }
            AddExpenseScreen(viewModel = viewModel)
        }
    }
}
