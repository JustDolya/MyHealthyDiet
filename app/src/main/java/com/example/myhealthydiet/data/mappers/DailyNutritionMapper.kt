package com.example.myhealthydiet.data.mappers

import com.example.myhealthydiet.data.local.room.entities.DailyNutritionEntity
import com.example.myhealthydiet.domain.models.DailyNutrition

fun DailyNutritionEntity.toDomain(): DailyNutrition {
    return DailyNutrition(
        userId = userId,
        date = date,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs
    )
}

fun DailyNutrition.toEntity(): DailyNutritionEntity {
    return DailyNutritionEntity(
        userId = userId,
        date = date,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs
    )
}