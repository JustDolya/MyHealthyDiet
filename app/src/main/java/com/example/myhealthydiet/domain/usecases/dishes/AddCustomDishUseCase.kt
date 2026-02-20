package com.example.myhealthydiet.domain.usecases.dishes

import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.domain.models.IngredientItem // ← изменили импорт
import com.example.myhealthydiet.domain.repository.DishRepository
import javax.inject.Inject

class AddCustomDishUseCase @Inject constructor(
    private val dishRepository: DishRepository
) {
    suspend operator fun invoke(
        name: String,
        categoryId: Int,
        minutesToCook: Int,
        ingredients: List<IngredientItem>, // ← изменили тип
        steps: String,
        calories: Int,
        proteins: Int,
        fats: Int,
        carbs: Int,
        imageUri: String?
    ): Result<Long> {
        return try {
            if (name.isBlank()) {
                return Result.failure(Exception("Название блюда не может быть пустым"))
            }

            if (calories < 0 || proteins < 0 || fats < 0 || carbs < 0) {
                return Result.failure(Exception("КБЖУ не могут быть отрицательными"))
            }

            if (steps.isBlank()) {
                return Result.failure(Exception("Добавьте описание приготовления"))
            }

            val dish = Dish(
                id = 0,
                categoryId = categoryId,
                name = name,
                minutesToCook = minutesToCook,
                ingredients = ingredients,
                steps = steps,
                calories = calories,
                proteins = proteins,
                fats = fats,
                carbs = carbs,
                imageUri = imageUri,
                isCustom = true
            )

            val dishId = dishRepository.addDish(dish)
            Result.success(dishId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}