package com.example.myhealthydiet.domain.repository

import com.example.myhealthydiet.domain.models.Product
import com.example.myhealthydiet.domain.models.ProductCategory
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductsByCategory(categoryId: Int): Flow<List<Product>>
    suspend fun getProductById(productId: Int): Product?
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun addProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    fun getAllCategories(): Flow<List<ProductCategory>>
    suspend fun getCategoryById(categoryId: Int): ProductCategory?
}