package com.example.myhealthydiet.domain.repository

import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.domain.models.DishCategory
import kotlinx.coroutines.flow.Flow

interface DishRepository {
    fun getAllDishes(): Flow<List<Dish>>
    fun getDishesByCategory(categoryId: Int): Flow<List<Dish>>
    suspend fun getDishById(dishId: Int): Dish?
    fun getUserCreatedDishes(): Flow<List<Dish>>
    fun searchDishes(query: String): Flow<List<Dish>>
    suspend fun addDish(dish: Dish): Long
    suspend fun updateDish(dish: Dish)
    suspend fun deleteDish(dish: Dish)
    fun getAllCategories(): Flow<List<DishCategory>>
    suspend fun getCategoryById(categoryId: Int): DishCategory?
}