package com.example.myhealthydiet.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.myhealthydiet.data.init.InitialDataLoader
import com.example.myhealthydiet.data.local.room.dao.DishCategoryDao
import com.example.myhealthydiet.data.local.room.dao.DishDao
import com.example.myhealthydiet.data.local.room.dao.ProductCategoryDao
import com.example.myhealthydiet.data.local.room.dao.ProductDao
import com.example.myhealthydiet.domain.repository.InitRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class InitRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productCategoryDao: ProductCategoryDao,
    private val dishCategoryDao: DishCategoryDao,
    private val productDao: ProductDao,
    private val dishDao: DishDao,
) : InitRepository {

    companion object {
        private val DATA_INITIALIZED = booleanPreferencesKey("data_initialized")
    }

    override suspend fun isDataInitialized(): Boolean {
        // Проверяем и флаг И реальное наличие данных в БД.
        // Флаг мог остаться true после переустановки, пока БД уже пуста —
        // поэтому одного флага недостаточно.
        val flagSet = context.dataStore.data
            .map { it[DATA_INITIALIZED] ?: false }
            .first()

        if (!flagSet) return false

        // Дополнительно проверяем что категории реально есть в БД
        val dishCatCount = dishCategoryDao.getCount()
        val productCatCount = productCategoryDao.getCount()

        return dishCatCount > 0 && productCatCount > 0
    }

    override suspend fun initializeCategories() {
        val productCategories = InitialDataLoader.getProductCategories()
        for (category in productCategories) {
            productCategoryDao.insertCategory(category)
        }

        val dishCategories = InitialDataLoader.getDishCategories()
        for (category in dishCategories) {
            dishCategoryDao.insertCategory(category)
        }
    }

    override suspend fun initializeStandardProducts() {
        val products = InitialDataLoader.getStandardProducts()
        productDao.insertProducts(products)
    }

    override suspend fun initializeStandardDishes() {
        val dishes = InitialDataLoader.getStandardDishes()
        dishDao.insertDishes(dishes)
    }

    override suspend fun markDataAsInitialized() {
        context.dataStore.edit { it[DATA_INITIALIZED] = true }
    }
}