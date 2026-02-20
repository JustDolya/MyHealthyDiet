package com.example.myhealthydiet.domain.usecases.nutrition

import com.example.myhealthydiet.domain.repository.NutritionRepository
import javax.inject.Inject

class CheckAndResetDailyNutritionUseCase @Inject constructor(
    private val nutritionRepository: NutritionRepository
) {
    suspend operator fun invoke() {
        // Проверяет, новый ли день, и если да - обнуляет КБЖУ
        nutritionRepository.checkAndResetIfNewDay()
    }
}