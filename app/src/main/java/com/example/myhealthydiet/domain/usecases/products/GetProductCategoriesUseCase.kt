package com.example.myhealthydiet.domain.usecases.products

import com.example.myhealthydiet.domain.models.ProductCategory
import com.example.myhealthydiet.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductCategoriesUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<ProductCategory>> {
        return productRepository.getAllCategories()
    }
}