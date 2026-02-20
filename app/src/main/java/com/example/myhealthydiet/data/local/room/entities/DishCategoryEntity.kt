package com.example.myhealthydiet.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish_categories")
data class DishCategoryEntity(
    @PrimaryKey val id: Int, // фиксированные ID (1-6)
    val name: String,
    val imagePath: String
)

// Стандартные категории блюд
object DishCategories {
    val STANDARD_CATEGORIES = listOf(
        DishCategoryEntity(id = 1, name = "Завтраки", imagePath = "ic_breakfast.png"),
        DishCategoryEntity(id = 2, name = "Первые блюда", imagePath = "ic_soup.png"),
        DishCategoryEntity(id = 3, name = "Вторые блюда", imagePath = "ic_main_course.png"),
        DishCategoryEntity(id = 4, name = "Салаты", imagePath = "ic_salad.png"),
        DishCategoryEntity(id = 5, name = "Десерты", imagePath = "ic_dessert.png"),
        DishCategoryEntity(id = 6, name = "Напитки", imagePath = "ic_drinks.png")
    )
}