package com.example.myhealthydiet.domain.usecases.auth

import com.example.myhealthydiet.domain.models.*
import com.example.myhealthydiet.domain.models.enums.*
import com.example.myhealthydiet.domain.repository.AuthRepository
import com.example.myhealthydiet.domain.repository.NutritionRepository
import com.example.myhealthydiet.domain.repository.UserRepository
import com.example.myhealthydiet.domain.usecases.init.InitializeAppUseCase
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val nutritionRepository: NutritionRepository,
    private val initializeAppUseCase: InitializeAppUseCase, // ← добавили
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

            // 2. Инициализация стандартных данных (категории, продукты, блюда)
            val initResult = initializeAppUseCase()
            if (initResult.isFailure) {
                // Если не удалось загрузить данные — всё равно продолжаем
                // (можно будет загрузить позже через синхронизацию)
            }

            // 3. Рассчитываем КБЖУ
            val nutrition = calculateNutritionUseCase(
                age = age,
                sex = sex,
                weight = weight,
                height = height,
                activityLevel = activityLevel,
                goal = goal
            )

            // 4. Создаем пользователя
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

            // 5. Сохраняем локально
            userRepository.saveUser(user)
            nutritionRepository.resetDailyNutrition()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}