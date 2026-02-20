package com.example.myhealthydiet.data.remote.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // User Profile
    suspend fun saveUserProfile(userId: String, data: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .set(data)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: String): Result<Map<String, Any>?> {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            Result.success(document.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Custom Products
    suspend fun saveCustomProduct(userId: String, productId: String, data: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("custom_products")
                .document(productId)
                .set(data)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCustomProducts(userId: String): Result<List<Map<String, Any>>> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("custom_products")
                .get()
                .await()

            val products = snapshot.documents.map { it.data ?: emptyMap() }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Custom Dishes
    suspend fun saveCustomDish(userId: String, dishId: String, data: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("custom_dishes")
                .document(dishId)
                .set(data)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCustomDishes(userId: String): Result<List<Map<String, Any>>> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("custom_dishes")
                .get()
                .await()

            val dishes = snapshot.documents.map { it.data ?: emptyMap() }
            Result.success(dishes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Consumption History
    suspend fun saveConsumptionHistory(userId: String, historyId: String, data: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("consumption_history")
                .document(historyId)
                .set(data)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getConsumptionHistory(userId: String): Result<List<Map<String, Any>>> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("consumption_history")
                .get()
                .await()

            val history = snapshot.documents.map { it.data ?: emptyMap() }
            Result.success(history)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Common Data (standard products, dishes, categories)
    suspend fun getCommonProducts(): Result<List<Map<String, Any>>> {
        return try {
            val snapshot = firestore.collection("common_data")
                .document("products")
                .collection("items")
                .get()
                .await()

            val products = snapshot.documents.map { it.data ?: emptyMap() }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCommonDishes(): Result<List<Map<String, Any>>> {
        return try {
            val snapshot = firestore.collection("common_data")
                .document("dishes")
                .collection("items")
                .get()
                .await()

            val dishes = snapshot.documents.map { it.data ?: emptyMap() }
            Result.success(dishes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Daily Nutrition
    suspend fun saveDailyNutrition(userId: String, data: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("daily_nutrition", data)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}