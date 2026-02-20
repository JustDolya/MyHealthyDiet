package com.example.myhealthydiet.data.local.room.dao

import androidx.room.*
import com.example.myhealthydiet.data.local.room.entities.DishCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DishCategoryDao {

    @Query("SELECT * FROM dish_categories ORDER BY id ASC")
    fun getAllCategories(): Flow<List<DishCategoryEntity>>

    @Query("SELECT * FROM dish_categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): DishCategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: DishCategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<DishCategoryEntity>)
}