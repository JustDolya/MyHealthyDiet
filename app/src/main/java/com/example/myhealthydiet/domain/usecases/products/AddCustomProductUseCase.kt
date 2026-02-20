package com.example.myhealthydiet.domain.usecases.products

import com.example.myhealthydiet.domain.models.Product
import com.example.myhealthydiet.domain.repository.ProductRepository
import javax.inject.Inject

class AddCustomProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        name: String,
        categoryId: Int,
        calories: Int,
        proteins: Int,
        fats: Int,
        carbs: Int
    ): Result<Long> {
        return try {
            if (name.isBlank()) {
                return Result.failure(Exception("Название продукта не может быть пустым"))
            }

            if (calories < 0 || proteins < 0 || fats < 0 || carbs < 0) {
                return Result.failure(Exception("КБЖУ не могут быть отрицательными"))
            }

            val product = Product(
                id = 0, // autoGenerate
                categoryId = categoryId,
                name = name,
                calories = calories,
                proteins = proteins,
                fats = fats,
                carbs = carbs,
                isCustom = true
            )

            val productId = productRepository.addProduct(product)
            Result.success(productId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}