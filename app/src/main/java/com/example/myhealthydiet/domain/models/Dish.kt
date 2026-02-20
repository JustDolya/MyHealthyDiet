package com.example.myhealthydiet.domain.models

data class Dish(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val minutesToCook: Int,
    val ingredients: List<IngredientItem>, // распарсенный JSON
    val steps: String,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int,
    val imageUri: String?,
    val isCustom: Boolean
)

data class IngredientItem(
    val name: String,    // "Молоко"
    val amount: String   // "200 мл"
)