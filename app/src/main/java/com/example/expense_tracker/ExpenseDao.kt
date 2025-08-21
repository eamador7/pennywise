package com.example.expense_tracker

import androidx.room.Dao
import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): PagingSource<Int, Expense>
}
