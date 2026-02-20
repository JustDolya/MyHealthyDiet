// data/repository/SyncRepositoryImpl.kt
package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.local.room.dao.*
import com.example.myhealthydiet.data.mappers.*
import com.example.myhealthydiet.data.remote.firebase.firestore.FirebaseFirestoreDataSource
import com.example.myhealthydiet.domain.repository.AuthRepository
import com.example.myhealthydiet.domain.repository.SyncRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
        private val authRepository: AuthRepository,
        private val firestoreDataSource: FirebaseFirestoreDataSource,
        private val userDao: UserDao,
        private val dailyNutritionDao: DailyNutritionDao,
        private val productDao: ProductDao,
        private val dishDao: DishDao,
        private val historyDao: ConsumptionHistoryDao
) : SyncRepository {

    override suspend fun syncAllData(): Result<Unit> {
        return try {
            val currentUser = authRepository.getCurrentUser()
                    ?: return Result.failure(Exception("User not logged in"))

            val userId = currentUser.uid

            // Синхронизация профиля
            syncUserProfile(userId)

            // Синхронизация дневной нормы
            syncDailyNutrition(userId)

            // Синхронизация пользовательских продуктов
            syncCustomProducts(userId)

            // Синхронизация пользовательских блюд
            syncCustomDishes(userId)

            // Синхронизация истории
            syncConsumptionHistory(userId)

            // Обновляем timestamp синхронизации
            userDao.updateSyncTimestamp(System.currentTimeMillis())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAndSyncIfNeeded(): Result<Unit> {
        return try {
            val currentUser = authRepository.getCurrentUser()
                    ?: return Result.failure(Exception("User not logged in"))

            val userId = currentUser.uid
            val localUser = userDao.getUserOnce() ?: return Result.success(Unit)

            // Получаем timestamp последнего обновления в облаке
            val profileResult = firestoreDataSource.getUserProfile(userId)
            if (profileResult.isFailure) {
                return Result.success(Unit) // Пропускаем, если не удалось получить данные
            }

            val profileData = profileResult.getOrNull() ?: return Result.success(Unit)
            val cloudTimestamp = profileData["lastUpdatedTimestamp"] as? Long ?: 0L
            val localTimestamp = localUser.lastSyncTimestamp ?: 0L

            // Если облако обновлено позже локальной синхронизации
            if (cloudTimestamp > localTimestamp) {
                // Загружаем данные из облака
                downloadAllData(userId)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAllData(): Result<Unit> {
        return try {
            val currentUser = authRepository.getCurrentUser()
                    ?: return Result.failure(Exception("User not logged in"))

            val userId = currentUser.uid

            // Загружаем данные в Firebase
            uploadUserProfile(userId)
            uploadDailyNutrition(userId)
            uploadCustomProducts(userId)
            uploadCustomDishes(userId)
            uploadConsumptionHistory(userId)

            // Обновляем timestamp
            userDao.updateSyncTimestamp(System.currentTimeMillis())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadAllData(): Result<Unit> {
        return try {
            val currentUser = authRepository.getCurrentUser()
                    ?: return Result.failure(Exception("User not logged in"))

            downloadAllData(currentUser.uid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadCommonData(): Result<Unit> {
        return try {
            // Загружаем стандартные продукты из Firebase
            val productsResult = firestoreDataSource.getCommonProducts()
            if (productsResult.isSuccess) {
                val products = productsResult.getOrNull()?.mapIndexed { index, map ->
                        map.toProduct(index).toEntity()
                } ?: emptyList()

                productDao.insertProducts(products)
            }

            // Загружаем стандартные блюда из Firebase
            val dishesResult = firestoreDataSource.getCommonDishes()
            if (dishesResult.isSuccess) {
                val dishes = dishesResult.getOrNull()?.mapIndexed { index, map ->
                        map.toDish(index).toEntity()
                } ?: emptyList()

                dishDao.insertDishes(dishes)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== Private Helper Methods ==========

    private suspend fun syncUserProfile(userId: String) {
        val localUser = userDao.getUserOnce() ?: return

                // Отправляем профиль в Firebase
                firestoreDataSource.saveUserProfile(
                        userId = userId,
                        data = localUser.toDomain().toFirebaseMap()
                )
    }

    private suspend fun syncDailyNutrition(userId: String) {
        val localNutrition = dailyNutritionDao.getDailyNutrition(1, getCurrentDate()).first()
                ?: return

                firestoreDataSource.saveDailyNutrition(
                        userId = userId,
                        data = localNutrition.toDomain().toFirebaseMap()
                )
    }

    private suspend fun syncCustomProducts(userId: String) {
        val customProducts = productDao.getAllProducts().first()
                .filter { it.isCustom }

        for (product in customProducts) {
            firestoreDataSource.saveCustomProduct(
                    userId = userId,
                    productId = product.id.toString(),
                    data = product.toDomain().toFirebaseMap()
            )
        }
    }

    private suspend fun syncCustomDishes(userId: String) {
        val customDishes = dishDao.getUserCreatedDishes().first()

        for (dish in customDishes) {
            firestoreDataSource.saveCustomDish(
                    userId = userId,
                    dishId = dish.id.toString(),
                    data = dish.toDomain().toFirebaseMap()
            )
        }
    }

    private suspend fun syncConsumptionHistory(userId: String) {
        val history = historyDao.getHistoryByUser(1).first()

        for (item in history) {
            firestoreDataSource.saveConsumptionHistory(
                    userId = userId,
                    historyId = item.id.toString(),
                    data = item.toDomain().toFirebaseMap()
            )
        }
    }

    private suspend fun uploadUserProfile(userId: String) {
        syncUserProfile(userId) // та же логика
    }

    private suspend fun uploadDailyNutrition(userId: String) {
        syncDailyNutrition(userId)
    }

    private suspend fun uploadCustomProducts(userId: String) {
        syncCustomProducts(userId)
    }

    private suspend fun uploadCustomDishes(userId: String) {
        syncCustomDishes(userId)
    }

    private suspend fun uploadConsumptionHistory(userId: String) {
        syncConsumptionHistory(userId)
    }

    private suspend fun downloadAllData(userId: String) {
        // Загружаем профиль
        val profileResult = firestoreDataSource.getUserProfile(userId)
        if (profileResult.isSuccess) {
            val profileData = profileResult.getOrNull()
            if (profileData != null) {
                val user = profileData.toUser(userId)
                userDao.insertUser(user.toEntity())
            }
        }

        // Загружаем пользовательские продукты
        val productsResult = firestoreDataSource.getCustomProducts(userId)
        if (productsResult.isSuccess) {
            val products = productsResult.getOrNull()?.mapIndexed { index, map ->
                    map.toProduct(index).toEntity()
            } ?: emptyList()

            productDao.insertProducts(products)
        }

        // Загружаем пользовательские блюда
        val dishesResult = firestoreDataSource.getCustomDishes(userId)
        if (dishesResult.isSuccess) {
            val dishes = dishesResult.getOrNull()?.mapIndexed { index, map ->
                    map.toDish(index).toEntity()
            } ?: emptyList()

            dishDao.insertDishes(dishes)
        }

        // Загружаем историю
        val historyResult = firestoreDataSource.getConsumptionHistory(userId)
        if (historyResult.isSuccess) {
            val history = historyResult.getOrNull()?.mapIndexed { index, map ->
                    map.toConsumptionHistory(index, 1).toEntity()
            } ?: emptyList()

            for (item in history) {
                historyDao.insertHistory(item)
            }
        }

        // Обновляем timestamp синхронизации
        userDao.updateSyncTimestamp(System.currentTimeMillis())
    }

    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}