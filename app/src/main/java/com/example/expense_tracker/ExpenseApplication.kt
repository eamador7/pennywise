package com.example.expense_tracker

import android.app.Application
import androidx.room.Room

class ExpenseApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java, "expense_database"
        ).build()
    }
}
