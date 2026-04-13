package com.example.myhealthydiet.ui.navigation

sealed class Screen(val route: String) {

    // Auth flow
    data object Login : Screen("login")
    data object Register : Screen("register")

    // Main flow (bottom nav)
    data object Home : Screen("home")
    data object Catalog : Screen("catalog")
    data object History : Screen("history")
    data object Profile : Screen("profile")

    // Catalog nested — блюда
    data object DishCategories : Screen("dish_categories")
    data object DishList : Screen("dish_list/{categoryId}/{categoryName}") {
        fun createRoute(categoryId: Int, categoryName: String) =
            "dish_list/$categoryId/${categoryName.encode()}"
    }
    data object DishDetail : Screen("dish_detail/{dishId}") {
        fun createRoute(dishId: Int) = "dish_detail/$dishId"
    }
    data object AddDish : Screen("add_dish/{categoryId}") {
        fun createRoute(categoryId: Int) = "add_dish/$categoryId"
    }

    // Catalog nested — продукты
    data object ProductCategories : Screen("product_categories")
    data object ProductList : Screen("product_list/{categoryId}/{categoryName}") {
        fun createRoute(categoryId: Int, categoryName: String) =
            "product_list/$categoryId/${categoryName.encode()}"
    }
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    data object AddProduct : Screen("add_product/{categoryId}") {
        fun createRoute(categoryId: Int) = "add_product/$categoryId"
    }
}

private fun String.encode() = java.net.URLEncoder.encode(this, "UTF-8")