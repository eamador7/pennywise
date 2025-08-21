package com.example.expense_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class AllExpensesViewModel(private val expenseDao: ExpenseDao) : ViewModel() {
    val expenses: Flow<PagingData<Expense>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { expenseDao.getAllExpenses() }
    ).flow.cachedIn(viewModelScope)
}

class AllExpensesViewModelFactory(private val expenseDao: ExpenseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AllExpensesViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
