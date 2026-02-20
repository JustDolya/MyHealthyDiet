package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.local.room.dao.ProductCategoryDao
import com.example.myhealthydiet.data.local.room.dao.ProductDao
import com.example.myhealthydiet.data.mappers.toDomain
import com.example.myhealthydiet.data.mappers.toEntity
import com.example.myhealthydiet.domain.models.Product
import com.example.myhealthydiet.domain.models.ProductCategory
import com.example.myhealthydiet.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productCategoryDao: ProductCategoryDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { list -> list.map { it.toDomain() } }
    }

    override fun getProductsByCategory(categoryId: Int): Flow<List<Product>> {
        return productDao.getProductsByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getProductById(productId: Int): Product? {
        return productDao.getProductById(productId)?.toDomain()
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addProduct(product: Product): Long {
        return productDao.insertProduct(product.toEntity())
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product.toEntity())
    }

    override fun getAllCategories(): Flow<List<ProductCategory>> {
        return productCategoryDao.getAllCategories().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getCategoryById(categoryId: Int): ProductCategory? {
        return productCategoryDao.getCategoryById(categoryId)?.toDomain()
    }
}