package com.example.myhealthydiet.domain.usecases.nutrition

import com.example.myhealthydiet.domain.models.DailyNutrition
import com.example.myhealthydiet.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyNutritionUseCase @Inject constructor(
    private val nutritionRepository: NutritionRepository
) {
    operator fun invoke(): Flow<DailyNutrition?> {
        return nutritionRepository.getDailyNutrition()
    }
}