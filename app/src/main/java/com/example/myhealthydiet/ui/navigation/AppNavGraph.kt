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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myhealthydiet.ui.screens.auth.LoginScreen
import com.example.myhealthydiet.ui.screens.auth.RegisterScreen
import com.example.myhealthydiet.ui.screens.catalog.AddDishScreen
import com.example.myhealthydiet.ui.screens.catalog.AddProductScreen
import com.example.myhealthydiet.ui.screens.catalog.CatalogScreen
import com.example.myhealthydiet.ui.screens.catalog.DishCategoriesScreen
import com.example.myhealthydiet.ui.screens.catalog.DishDetailScreen
import com.example.myhealthydiet.ui.screens.catalog.DishListScreen
import com.example.myhealthydiet.ui.screens.catalog.ProductCategoriesScreen
import com.example.myhealthydiet.ui.screens.catalog.ProductDetailScreen
import com.example.myhealthydiet.ui.screens.catalog.ProductListScreen
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
                                    imageVector = if (isSelected) item.selectedIcon
                                    else item.unselectedIcon,
                                    contentDescription = item.label,
                                )
                            },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            // Auth
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            // Main
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.History.route) { HistoryScreen(navController) }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    navController = navController,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                )
            }

            // Catalog root
            composable(Screen.Catalog.route) { CatalogScreen(navController) }

            // Dishes
            composable(Screen.DishCategories.route) { DishCategoriesScreen(navController) }
            composable(
                route = Screen.DishList.route,
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.IntType },
                    navArgument("categoryName") { type = NavType.StringType },
                ),
            ) { back ->
                val categoryId = back.arguments?.getInt("categoryId") ?: 0
                val categoryName = back.arguments?.getString("categoryName") ?: ""
                DishListScreen(
                    navController = navController,
                    categoryId = categoryId,
                    categoryName = java.net.URLDecoder.decode(categoryName, "UTF-8"),
                )
            }
            composable(
                route = Screen.DishDetail.route,
                arguments = listOf(navArgument("dishId") { type = NavType.IntType }),
            ) { back ->
                DishDetailScreen(
                    navController = navController,
                    dishId = back.arguments?.getInt("dishId") ?: 0,
                )
            }
            composable(
                route = Screen.AddDish.route,
                arguments = listOf(navArgument("categoryId") { type = NavType.IntType }),
            ) { back ->
                AddDishScreen(
                    navController = navController,
                    initialCategoryId = back.arguments?.getInt("categoryId") ?: 0,
                )
            }

            // Products
            composable(Screen.ProductCategories.route) { ProductCategoriesScreen(navController) }
            composable(
                route = Screen.ProductList.route,
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.IntType },
                    navArgument("categoryName") { type = NavType.StringType },
                ),
            ) { back ->
                val categoryId = back.arguments?.getInt("categoryId") ?: 0
                val categoryName = back.arguments?.getString("categoryName") ?: ""
                ProductListScreen(
                    navController = navController,
                    categoryId = categoryId,
                    categoryName = java.net.URLDecoder.decode(categoryName, "UTF-8"),
                )
            }
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.IntType }),
            ) { back ->
                ProductDetailScreen(
                    navController = navController,
                    productId = back.arguments?.getInt("productId") ?: 0,
                )
            }
            composable(
                route = Screen.AddProduct.route,
                arguments = listOf(navArgument("categoryId") { type = NavType.IntType }),
            ) { back ->
                AddProductScreen(
                    navController = navController,
                    initialCategoryId = back.arguments?.getInt("categoryId") ?: 0,
                )
            }
        }
    }
}