package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.local.room.dao.DishCategoryDao
import com.example.myhealthydiet.data.local.room.dao.DishDao
import com.example.myhealthydiet.data.mappers.toDomain
import com.example.myhealthydiet.data.mappers.toEntity
import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.domain.models.DishCategory
import com.example.myhealthydiet.domain.repository.DishRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DishRepositoryImpl @Inject constructor(
    private val dishDao: DishDao,
    private val dishCategoryDao: DishCategoryDao
) : DishRepository {

    override fun getAllDishes(): Flow<List<Dish>> {
        return dishDao.getAllDishes().map { list -> list.map { it.toDomain() } }
    }

    override fun getDishesByCategory(categoryId: Int): Flow<List<Dish>> {
        return dishDao.getDishesByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getDishById(dishId: Int): Dish? {
        return dishDao.getDishById(dishId)?.toDomain()
    }

    override fun getUserCreatedDishes(): Flow<List<Dish>> {
        return dishDao.getUserCreatedDishes().map { list -> list.map { it.toDomain() } }
    }

    override fun searchDishes(query: String): Flow<List<Dish>> {
        return dishDao.searchDishes(query).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addDish(dish: Dish): Long {
        return dishDao.insertDish(dish.toEntity())
    }

    override suspend fun updateDish(dish: Dish) {
        dishDao.updateDish(dish.toEntity())
    }

    override suspend fun deleteDish(dish: Dish) {
        dishDao.deleteDish(dish.toEntity())
    }

    override fun getAllCategories(): Flow<List<DishCategory>> {
        return dishCategoryDao.getAllCategories().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getCategoryById(categoryId: Int): DishCategory? {
        return dishCategoryDao.getCategoryById(categoryId)?.toDomain()
    }
}