package com.example.myhealthydiet.domain.models

data class DailyNutrition(
    val userId: Int,
    val date: String,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int
)