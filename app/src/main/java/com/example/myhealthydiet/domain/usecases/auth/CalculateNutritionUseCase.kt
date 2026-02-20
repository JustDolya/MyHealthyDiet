package com.example.myhealthydiet.domain.usecases.auth

import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal
import javax.inject.Inject
import kotlin.math.roundToInt

data class NutritionGoals(
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int
)

class CalculateNutritionUseCase @Inject constructor() {

    operator fun invoke(
        age: Int,
        sex: Gender,
        weight: Int,
        height: Int,
        activityLevel: ActivityLevel,
        goal: Goal
    ): NutritionGoals {

        // Формула Mifflin-St Jeor для базового метаболизма (BMR)
        val bmr = when (sex) {
            Gender.MALE -> 10 * weight + 6.25 * height - 5 * age + 5
            Gender.FEMALE -> 10 * weight + 6.25 * height - 5 * age - 161
        }

        // Учитываем уровень активности
        val activityMultiplier = when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2
            ActivityLevel.LIGHT -> 1.375
            ActivityLevel.MODERATE -> 1.55
            ActivityLevel.ACTIVE -> 1.725
            ActivityLevel.VERY_ACTIVE -> 1.9
        }

        val tdee = bmr * activityMultiplier

        // Учитываем цель
        val calories = when (goal) {
            Goal.LOSE_WEIGHT -> (tdee - 500).roundToInt() // дефицит 500 ккал
            Goal.MAINTAIN -> tdee.roundToInt()
            Goal.GAIN_WEIGHT -> (tdee + 300).roundToInt() // профицит 300 ккал
        }

        // --- РАСЧЁТ БЖУ НА ОСНОВЕ НАУЧНЫХ РЕКОМЕНДАЦИЙ ---

// 1. Белок (граммы в сутки)
// Основано на метаанализах Morton 2018, Helms 2014
        val proteinsPerKg = when (goal) {
            Goal.LOSE_WEIGHT -> 2.0    // защита мышц в дефиците
            Goal.MAINTAIN -> 1.2       // поддержание и восстановление
            Goal.GAIN_WEIGHT -> 1.7    // плато роста ≈ 1.6–1.8 г/кг
        }

        val proteins = (weight * proteinsPerKg).roundToInt()

        val fatsPerKg = when (goal) {
            Goal.LOSE_WEIGHT -> 0.7
            Goal.MAINTAIN -> 0.9
            Goal.GAIN_WEIGHT -> 1.0
        }

        val fats = (weight * fatsPerKg).roundToInt()

        val proteinsCalories = proteins * 4
        val fatsCalories = fats * 9

        val carbsCalories = calories - proteinsCalories - fatsCalories

        val carbs = maxOf(0, (carbsCalories / 4))

        return NutritionGoals(
            calories = calories,
            proteins = proteins,
            fats = fats,
            carbs = carbs
        )

    }
}