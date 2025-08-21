package com.example.expense_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AddExpenseViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    fun saveExpense(
        amount: Double,
        currency: String,
        category: String,
        date: Long,
        description: String?
    ) {
        viewModelScope.launch {
            val expense = Expense(
                amount = amount,
                currency = currency,
                category = category,
                date = date,
                description = description
            )
            expenseDao.insert(expense)
        }
    }
}

class AddExpenseViewModelFactory(private val expenseDao: ExpenseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
