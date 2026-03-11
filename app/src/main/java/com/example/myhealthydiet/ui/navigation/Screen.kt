package com.example.myhealthydiet.ui.navigation

sealed class Screen(val route: String) {

    // Auth flow
    data object Login : Screen("login")
    data object Register : Screen("register")

    // Main flow (bottom nav)
    data object Home : Screen("home")
    data object Diary : Screen("diary")
    data object Catalog : Screen("catalog")
    data object History : Screen("history")
    data object Profile : Screen("profile")

    // Nested screens
    data object AddProduct : Screen("add_product")
    data object AddDish : Screen("add_dish")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Long) = "product_detail/$productId"
    }
    data object DishDetail : Screen("dish_detail/{dishId}") {
        fun createRoute(dishId: Long) = "dish_detail/$dishId"
    }
}
