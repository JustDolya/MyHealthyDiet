package com.example.myhealthydiet.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myhealthydiet.data.local.room.entities.ProductCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCategoryDao {

    @Query("SELECT * FROM product_categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<ProductCategoryEntity>>

    @Query("SELECT * FROM product_categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): ProductCategoryEntity?

    @Query("SELECT COUNT(*) FROM product_categories")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: ProductCategoryEntity): Long

    @Update
    suspend fun updateCategory(category: ProductCategoryEntity)

    @Delete
    suspend fun deleteCategory(category: ProductCategoryEntity)
}