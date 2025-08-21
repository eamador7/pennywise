package com.example.expense_tracker

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ExpenseDaoTest {

    private lateinit var expenseDao: ExpenseDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        expenseDao = db.expenseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetExpense() = runBlocking {
        val expense = Expense(
            amount = 100.0,
            currency = "USD",
            category = "Food",
            date = System.currentTimeMillis(),
            description = "Lunch"
        )
        expenseDao.insert(expense)
        val allExpenses = expenseDao.getAllExpenses().first()
        assertEquals(allExpenses[0].category, "Food")
    }
}
