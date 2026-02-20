package com.example.myhealthydiet.domain.repository

import com.example.myhealthydiet.domain.models.DailyNutrition
import kotlinx.coroutines.flow.Flow

interface NutritionRepository {
    fun getDailyNutrition(): Flow<DailyNutrition?>
    suspend fun getDailyNutritionOnce(): DailyNutrition?
    suspend fun resetDailyNutrition()
    suspend fun subtractNutrition(calories: Int, proteins: Int, fats: Int, carbs: Int)
    suspend fun checkAndResetIfNewDay()
}