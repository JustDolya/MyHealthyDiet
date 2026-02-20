package com.example.myhealthydiet.data.local.room.dao

import androidx.room.*
import com.example.myhealthydiet.data.local.room.entities.DishEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {

    @Query("SELECT * FROM dishes ORDER BY name ASC")
    fun getAllDishes(): Flow<List<DishEntity>>

    @Query("SELECT * FROM dishes WHERE categoryId = :categoryId ORDER BY name ASC")
    fun getDishesByCategory(categoryId: Int): Flow<List<DishEntity>>

    @Query("SELECT * FROM dishes WHERE id = :dishId")
    suspend fun getDishById(dishId: Int): DishEntity?

    @Query("SELECT * FROM dishes WHERE isCreated = 1 ORDER BY name ASC")
    fun getUserCreatedDishes(): Flow<List<DishEntity>>

    @Query("SELECT * FROM dishes WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchDishes(query: String): Flow<List<DishEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: DishEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(dishes: List<DishEntity>)

    @Update
    suspend fun updateDish(dish: DishEntity)

    @Delete
    suspend fun deleteDish(dish: DishEntity)
}