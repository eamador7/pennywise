package com.example.expense_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val application = application as ExpenseApplication
            val addExpenseViewModel: AddExpenseViewModel by viewModels {
                AddExpenseViewModelFactory(application.database.expenseDao())
            }
            val allExpensesViewModel: AllExpensesViewModel by viewModels {
                AllExpensesViewModelFactory(application.database.expenseDao())
            }

            val navController = rememberNavController()
            val screens = listOf(
                Screen.AddExpense,
                Screen.AllExpenses
            )

            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        screens.forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    when (screen) {
                                        Screen.AddExpense -> Icon(Icons.Default.Add, contentDescription = null)
                                        Screen.AllExpenses -> Icon(Icons.Default.List, contentDescription = null)
                                    }
                                },
                                label = { Text(screen.route.replaceFirstChar { it.uppercase() }) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.AddExpense.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.AddExpense.route) {
                        AddExpenseScreen(viewModel = addExpenseViewModel)
                    }
                    composable(Screen.AllExpenses.route) {
                        AllExpensesScreen(viewModel = allExpensesViewModel)
                    }
                }
            }
        }
    }
}
