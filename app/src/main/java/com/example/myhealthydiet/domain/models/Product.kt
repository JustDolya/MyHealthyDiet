package com.example.myhealthydiet.domain.models

data class Product(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int,
    val isCustom: Boolean
)