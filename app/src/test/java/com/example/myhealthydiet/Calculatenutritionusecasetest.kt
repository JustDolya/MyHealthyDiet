package com.example.myhealthydiet.domain.usecases.auth

import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Тесты формулы расчёта КБЖУ
 * Зависимостей нет
 */
class CalculateNutritionUseCaseTest {

    private lateinit var useCase: CalculateNutritionUseCase

    @Before
    fun setUp() {
        useCase = CalculateNutritionUseCase()
    }

    // ── Базовые расчёты ───────────────────────────────────────────────────────

    @Test
    fun `мужчина 25 лет 80кг 180см средняя активность поддержание`() {
        val result = useCase(
            age = 25, sex = Gender.MALE, weight = 80, height = 180,
            activityLevel = ActivityLevel.MODERATE, goal = Goal.MAINTAIN
        )
        // BMR = 10*80 + 6.25*180 - 5*25 + 5 = 1880
        // TDEE = 1880 * 1.55 ≈ 2914
        assertTrue("Калории должны быть в диапазоне 2700–3100", result.calories in 2700..3100)
        assertTrue("Белки должны быть положительными", result.proteins > 0)
        assertTrue("Жиры должны быть положительными", result.fats > 0)
        assertTrue("Углеводы должны быть положительными", result.carbs > 0)
    }

    @Test
    fun `женщина 30 лет 60кг 165см лёгкая активность похудение`() {
        val result = useCase(
            age = 30, sex = Gender.FEMALE, weight = 60, height = 165,
            activityLevel = ActivityLevel.LIGHT, goal = Goal.LOSE_WEIGHT
        )
        // BMR = 10*60 + 6.25*165 - 5*30 - 161 = 1370.25
        // TDEE = 1370.25 * 1.375 ≈ 1884
        // Похудение = TDEE - 300 ≈ 1584
        assertTrue("При похудении калорий должно быть меньше TDEE", result.calories < 1900)
        assertTrue("Калории не должны уходить в ноль", result.calories > 1200)
    }

    @Test
    fun `набор веса даёт больше калорий чем поддержание`() {
        val base = useCase(
            age = 25, sex = Gender.MALE, weight = 70, height = 175,
            activityLevel = ActivityLevel.MODERATE, goal = Goal.MAINTAIN
        )
        val gain = useCase(
            age = 25, sex = Gender.MALE, weight = 70, height = 175,
            activityLevel = ActivityLevel.MODERATE, goal = Goal.GAIN_WEIGHT
        )
        assertTrue("Набор должен давать больше калорий чем поддержание",
            gain.calories > base.calories)
    }

    @Test
    fun `похудение даёт меньше калорий чем поддержание`() {
        val base = useCase(
            age = 25, sex = Gender.FEMALE, weight = 65, height = 168,
            activityLevel = ActivityLevel.MODERATE, goal = Goal.MAINTAIN
        )
        val lose = useCase(
            age = 25, sex = Gender.FEMALE, weight = 65, height = 168,
            activityLevel = ActivityLevel.MODERATE, goal = Goal.LOSE_WEIGHT
        )
        assertTrue("Похудение должно давать меньше калорий чем поддержание",
            lose.calories < base.calories)
    }

    @Test
    fun `очень высокая активность даёт больше калорий чем сидячий образ жизни`() {
        val sedentary = useCase(
            age = 30, sex = Gender.MALE, weight = 75, height = 175,
            activityLevel = ActivityLevel.SEDENTARY, goal = Goal.MAINTAIN
        )
        val veryActive = useCase(
            age = 30, sex = Gender.MALE, weight = 75, height = 175,
            activityLevel = ActivityLevel.VERY_ACTIVE, goal = Goal.MAINTAIN
        )
        assertTrue("Очень высокая активность должна давать больше калорий",
            veryActive.calories > sedentary.calories)
    }

    @Test
    fun `сумма КБЖУ в граммах соответствует заявленным калориям`() {
        val result = useCase(
            age = 25, sex = Gender.MALE, weight = 80, height = 180,
            activityLevel = ActivityLevel.MODERATE, goal = Goal.MAINTAIN
        )
        // Белки и углеводы = 4 ккал/г, жиры = 9 ккал/г
        val calculatedCalories = result.proteins * 4 + result.fats * 9 + result.carbs * 4
        // Допускаем погрешность ±100 ккал из-за округления
        assertEquals("Сумма КБЖУ должна соответствовать калориям",
            result.calories.toDouble(), calculatedCalories.toDouble(), 100.0)
    }
}