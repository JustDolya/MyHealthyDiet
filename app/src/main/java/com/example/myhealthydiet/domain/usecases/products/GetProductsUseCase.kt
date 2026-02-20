package com.example.myhealthydiet.domain.usecases.products

import com.example.myhealthydiet.domain.models.Product
import com.example.myhealthydiet.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getAllProducts()
    }

    fun byCategory(categoryId: Int): Flow<List<Product>> {
        return productRepository.getProductsByCategory(categoryId)
    }
}