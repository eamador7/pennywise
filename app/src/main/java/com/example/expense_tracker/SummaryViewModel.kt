package com.example.expense_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.Calendar

enum class TimeFilter {
    DAY, WEEK, MONTH, YEAR
}

@OptIn(ExperimentalCoroutinesApi::class)
class SummaryViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    private val _startDate = MutableStateFlow(getStartOfWeek())
    private val _endDate = MutableStateFlow(System.currentTimeMillis())
    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _selectedCurrency = MutableStateFlow<String?>(null)

    val uniqueCategories: StateFlow<List<String>> = expenseDao.getUniqueCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uniqueCurrencies: StateFlow<List<String>> = expenseDao.getUniqueCurrencies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredExpenses: StateFlow<List<Expense>> = combine(
        _startDate,
        _endDate,
        _selectedCategory,
        _selectedCurrency
    ) { startDate, endDate, category, currency ->
        Triple(startDate..endDate, category, currency)
    }.flatMapLatest { (dateRange, category, currency) ->
        expenseDao.getFilteredExpenses(dateRange.start, dateRange.endInclusive, currency, category)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setDateFilter(filter: TimeFilter) {
        val calendar = Calendar.getInstance()
        _endDate.value = calendar.timeInMillis

        when (filter) {
            TimeFilter.DAY -> calendar.add(Calendar.DAY_OF_YEAR, -1)
            TimeFilter.WEEK -> calendar.add(Calendar.WEEK_OF_YEAR, -1)
            TimeFilter.MONTH -> calendar.add(Calendar.MONTH, -1)
            TimeFilter.YEAR -> calendar.add(Calendar.YEAR, -1)
        }
        _startDate.value = calendar.timeInMillis
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = if (category == "All") null else category
    }

    fun setCurrency(currency: String?) {
        _selectedCurrency.value = if (currency == "All") null else currency
    }

    companion object {
        private fun getStartOfWeek(): Long {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            return calendar.timeInMillis
        }
    }
}

class SummaryViewModelFactory(private val expenseDao: ExpenseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SummaryViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
