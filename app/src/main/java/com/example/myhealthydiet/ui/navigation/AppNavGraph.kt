package com.example.myhealthydiet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import com.example.myhealthydiet.ui.screens.auth.LoginScreen
import com.example.myhealthydiet.ui.screens.auth.RegisterScreen
import com.example.myhealthydiet.ui.screens.catalog.CatalogScreen
import com.example.myhealthydiet.ui.screens.diary.DiaryScreen
import com.example.myhealthydiet.ui.screens.history.HistoryScreen
import com.example.myhealthydiet.ui.screens.home.HomeScreen
import com.example.myhealthydiet.ui.screens.profile.ProfileScreen

@Composable
fun AppNavGraph(isLoggedIn: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route

    val mainScreenRoutes = bottomNavItems.map { it.screen.route }.toSet()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isMainScreen = currentDestination?.route in mainScreenRoutes

    Scaffold(
        bottomBar = {
            if (isMainScreen) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy
                            ?.any { it.route == item.screen.route } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Main screens
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Diary.route) {
                DiaryScreen(navController = navController)
            }
            composable(Screen.Catalog.route) {
                CatalogScreen(navController = navController)
            }
            composable(Screen.History.route) {
                HistoryScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    navController = navController,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
