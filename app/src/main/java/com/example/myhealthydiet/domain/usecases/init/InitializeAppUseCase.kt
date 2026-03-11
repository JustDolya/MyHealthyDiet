package com.example.myhealthydiet.domain.usecases.init

import com.example.myhealthydiet.domain.repository.InitRepository
import javax.inject.Inject

class InitializeAppUseCase @Inject constructor(
    private val initRepository: InitRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            // Проверяем, инициализированы ли данные
            if (initRepository.isDataInitialized()) {
                return Result.success(Unit) // Данные уже загружены
            }

            // Инициализация категорий (обязательно первыми, т.к. продукты и блюда на них ссылаются)
            initRepository.initializeCategories()

            // Инициализация стандартных продуктов (23 штуки)
            initRepository.initializeStandardProducts()

            // Инициализация стандартных блюд (18 штук)
            initRepository.initializeStandardDishes()

            // Помечаем, что данные загружены
            initRepository.markDataAsInitialized()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}