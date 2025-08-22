package com.example.expense_tracker

import androidx.room.Dao
import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): PagingSource<Int, Expense>

    @Query("SELECT * FROM expenses ORDER BY date DESC LIMIT 5")
    fun getRecentExpenses(): Flow<List<Expense>>

    @Query("""
        SELECT * FROM expenses
        WHERE date >= :startDate AND date <= :endDate
        AND (:currency IS NULL OR currency = :currency)
        AND (:category IS NULL OR category = :category)
        ORDER BY date DESC
    """)
    fun getFilteredExpenses(startDate: Long, endDate: Long, currency: String?, category: String?): Flow<List<Expense>>

    @Query("SELECT DISTINCT category FROM expenses ORDER BY category ASC")
    fun getUniqueCategories(): Flow<List<String>>

    @Query("SELECT DISTINCT currency FROM expenses ORDER BY currency ASC")
    fun getUniqueCurrencies(): Flow<List<String>>
}
