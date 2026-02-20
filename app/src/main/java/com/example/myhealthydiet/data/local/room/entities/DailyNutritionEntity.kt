package com.example.myhealthydiet.data.local.room.entities

import androidx.room.Entity

@Entity(
    tableName = "daily_nutrition_remaining",
    primaryKeys = ["userId", "date"]
)
data class DailyNutritionEntity(
    val userId: Int,
    val date: String, // "2026-01-20" формат
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int
)