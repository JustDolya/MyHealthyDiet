package com.example.myhealthydiet.domain.usecases.history

import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.domain.repository.DishRepository
import com.example.myhealthydiet.domain.repository.HistoryRepository
import com.example.myhealthydiet.domain.repository.NutritionRepository
import com.example.myhealthydiet.domain.repository.ProductRepository
import javax.inject.Inject
import kotlin.math.roundToInt

class AddConsumptionUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val productRepository: ProductRepository,
    private val dishRepository: DishRepository,
    private val nutritionRepository: NutritionRepository
) {
    suspend operator fun invoke(
        foodId: Int,
        isDish: Boolean,
        grams: Int
    ): Result<Unit> {
        return try {
            if (grams <= 0) {
                return Result.failure(Exception("Количество должно быть больше 0"))
            }

            val (name, calories, proteins, fats, carbs) = if (isDish) {
                // Это блюдо
                val dish = dishRepository.getDishById(foodId)
                    ?: return Result.failure(Exception("Блюдо не найдено"))

                // Для блюда grams - это процент порции (100 = целое блюдо)
                val multiplier = grams / 100.0
                Tuple5(
                    dish.name,
                    (dish.calories * multiplier).roundToInt(),
                    (dish.proteins * multiplier).roundToInt(),
                    (dish.fats * multiplier).roundToInt(),
                    (dish.carbs * multiplier).roundToInt()
                )
            } else {
                // Это продукт
                val product = productRepository.getProductById(foodId)
                    ?: return Result.failure(Exception("Продукт не найден"))

                // Для продукта grams - это граммы
                val multiplier = grams / 100.0
                Tuple5(
                    product.name,
                    (product.calories * multiplier).roundToInt(),
                    (product.proteins * multiplier).roundToInt(),
                    (product.fats * multiplier).roundToInt(),
                    (product.carbs * multiplier).roundToInt()
                )
            }

            // Создаем запись в истории
            val history = ConsumptionHistory(
                id = 0,
                userId = 1,
                datetime = System.currentTimeMillis(),
                foodId = foodId,
                isDish = isDish,
                name = name,
                grams = grams,
                calories = calories,
                proteins = proteins,
                fats = fats,
                carbs = carbs
            )

            historyRepository.addConsumption(history)

            // Вычитаем из дневной нормы
            nutritionRepository.subtractNutrition(calories, proteins, fats, carbs)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private data class Tuple5(
        val name: String,
        val calories: Int,
        val proteins: Int,
        val fats: Int,
        val carbs: Int
    )
}