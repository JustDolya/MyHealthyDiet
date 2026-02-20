package com.example.myhealthydiet.domain.usecases.products

import com.example.myhealthydiet.domain.models.Product
import com.example.myhealthydiet.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(query: String): Flow<List<Product>> {
        return productRepository.searchProducts(query)
    }
}