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

// Extension для DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class InitRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productCategoryDao: ProductCategoryDao,
    private val dishCategoryDao: DishCategoryDao,
    private val productDao: ProductDao,
    private val dishDao: DishDao
) : InitRepository {

    companion object {
        private val DATA_INITIALIZED = booleanPreferencesKey("data_initialized")
    }

    override suspend fun isDataInitialized(): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[DATA_INITIALIZED] ?: false
        }.first()
    }

    override suspend fun initializeCategories() {
        // Загружаем категории продуктов (15 штук)
        val productCategories = InitialDataLoader.getProductCategories()
        for (category in productCategories) {
            productCategoryDao.insertCategory(category)
        }

        // Загружаем категории блюд (6 штук)
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
        context.dataStore.edit { preferences ->
            preferences[DATA_INITIALIZED] = true
        }
    }
}