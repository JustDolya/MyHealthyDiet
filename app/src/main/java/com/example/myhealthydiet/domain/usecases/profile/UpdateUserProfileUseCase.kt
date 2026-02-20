package com.example.myhealthydiet.domain.usecases.profile

import com.example.myhealthydiet.domain.models.User
import com.example.myhealthydiet.domain.repository.NutritionRepository
import com.example.myhealthydiet.domain.repository.UserRepository
import com.example.myhealthydiet.domain.usecases.auth.CalculateNutritionUseCase
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val nutritionRepository: NutritionRepository,
    private val calculateNutritionUseCase: CalculateNutritionUseCase
) {
    suspend operator fun invoke(
        user: User,
        recalculateNutrition: Boolean = false
    ): Result<Unit> {
        return try {
            val updatedUser = if (recalculateNutrition) {
                // Пересчитываем КБЖУ при изменении параметров
                val nutrition = calculateNutritionUseCase(
                    age = user.age,
                    sex = user.sex,
                    weight = user.weight,
                    height = user.height,
                    activityLevel = user.activityLevel,
                    goal = user.goal
                )

                user.copy(
                    calories = nutrition.calories,
                    proteins = nutrition.proteins,
                    fats = nutrition.fats,
                    carbs = nutrition.carbs
                )
            } else {
                user
            }

            userRepository.updateUser(updatedUser)

            // Если пересчитали КБЖУ - обновляем дневную норму
            if (recalculateNutrition) {
                nutritionRepository.resetDailyNutrition()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}