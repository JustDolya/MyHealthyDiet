package com.example.myhealthydiet.domain.usecases.auth

import com.example.myhealthydiet.domain.models.*
import com.example.myhealthydiet.domain.models.enums.*
import com.example.myhealthydiet.domain.repository.AuthRepository
import com.example.myhealthydiet.domain.repository.NutritionRepository
import com.example.myhealthydiet.domain.repository.SyncRepository
import com.example.myhealthydiet.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val nutritionRepository: NutritionRepository,
    private val syncRepository: SyncRepository,
    private val calculateNutritionUseCase: CalculateNutritionUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        age: Int,
        sex: Gender,
        weight: Int,
        height: Int,
        activityLevel: ActivityLevel,
        goal: Goal
    ): Result<User> {
        return try {
            // 1. Регистрация в Firebase Auth
            val firebaseUserResult = authRepository.registerWithEmail(email, password)
            if (firebaseUserResult.isFailure) {
                return Result.failure(firebaseUserResult.exceptionOrNull()!!)
            }

            val firebaseUser = firebaseUserResult.getOrNull()!!

            // 2. Рассчитываем КБЖУ
            val nutrition = calculateNutritionUseCase(
                age = age,
                sex = sex,
                weight = weight,
                height = height,
                activityLevel = activityLevel,
                goal = goal
            )

            // 3. Создаем пользователя
            val user = User(
                id = 1,
                firebaseUid = firebaseUser.uid,
                email = email,
                age = age,
                sex = sex,
                weight = weight,
                height = height,
                activityLevel = activityLevel,
                goal = goal,
                calories = nutrition.calories,
                proteins = nutrition.proteins,
                fats = nutrition.fats,
                carbs = nutrition.carbs,
                lastSyncTimestamp = System.currentTimeMillis()
            )

            // 4. Сохраняем локально
            userRepository.saveUser(user)
            nutritionRepository.resetDailyNutrition()

            // 5. Загружаем стандартные данные (продукты, блюда)
            syncRepository.loadCommonData()

            // 6. Загружаем данные в Firebase
            syncRepository.uploadAllData()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}