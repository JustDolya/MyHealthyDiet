package com.example.myhealthydiet.data.mappers

import com.example.myhealthydiet.domain.models.*
import com.example.myhealthydiet.domain.models.enums.*

// User to Firebase Map
fun User.toFirebaseMap(): Map<String, Any> {
    return mapOf(
        "email" to email,
        "age" to age,
        "sex" to sex.value,
        "weight" to weight,
        "height" to height,
        "activityLevel" to activityLevel.value,
        "goal" to goal.value,
        "calories" to calories,
        "proteins" to proteins,
        "fats" to fats,
        "carbs" to carbs,
        "lastUpdatedTimestamp" to (lastSyncTimestamp ?: System.currentTimeMillis())
    )
}

// Firebase Map to User
fun Map<String, Any>.toUser(firebaseUid: String): User {
    return User(
        id = 1,
        firebaseUid = firebaseUid,
        email = this["email"] as? String ?: "",
        age = (this["age"] as? Long)?.toInt() ?: 0,
        sex = Gender.fromInt((this["sex"] as? Long)?.toInt() ?: 0),
        weight = (this["weight"] as? Long)?.toInt() ?: 0,
        height = (this["height"] as? Long)?.toInt() ?: 0,
        activityLevel = ActivityLevel.fromInt((this["activityLevel"] as? Long)?.toInt() ?: 0),
        goal = Goal.fromInt((this["goal"] as? Long)?.toInt() ?: 0),
        calories = (this["calories"] as? Long)?.toInt() ?: 0,
        proteins = (this["proteins"] as? Long)?.toInt() ?: 0,
        fats = (this["fats"] as? Long)?.toInt() ?: 0,
        carbs = (this["carbs"] as? Long)?.toInt() ?: 0,
        lastSyncTimestamp = this["lastUpdatedTimestamp"] as? Long
    )
}

// Product to Firebase Map
fun Product.toFirebaseMap(): Map<String, Any> {
    return mapOf(
        "categoryId" to categoryId,
        "name" to name,
        "calories" to calories,
        "proteins" to proteins,
        "fats" to fats,
        "carbs" to carbs,
        "isCustom" to isCustom
    )
}

// Firebase Map to Product
fun Map<String, Any>.toProduct(id: Int): Product {
    return Product(
        id = id,
        categoryId = (this["categoryId"] as? Long)?.toInt() ?: 0,
        name = this["name"] as? String ?: "",
        calories = (this["calories"] as? Long)?.toInt() ?: 0,
        proteins = (this["proteins"] as? Long)?.toInt() ?: 0,
        fats = (this["fats"] as? Long)?.toInt() ?: 0,
        carbs = (this["carbs"] as? Long)?.toInt() ?: 0,
        isCustom = this["isCustom"] as? Boolean ?: false
    )
}

// Dish to Firebase Map
fun Dish.toFirebaseMap(): Map<String, Any> {
    return mapOf(
        "categoryId" to categoryId,
        "name" to name,
        "minutesToCook" to minutesToCook,
        "ingredients" to ingredients.map { mapOf("name" to it.name, "amount" to it.amount) },
        "steps" to steps,
        "calories" to calories,
        "proteins" to proteins,
        "fats" to fats,
        "carbs" to carbs,
        "imageUri" to (imageUri ?: ""),
        "isCustom" to isCustom
    )
}

// Firebase Map to Dish
@Suppress("UNCHECKED_CAST")
fun Map<String, Any>.toDish(id: Int): Dish {
    val ingredientsList = (this["ingredients"] as? List<Map<String, Any>>)?.map {
        IngredientItem(
            name = it["name"] as? String ?: "",
            amount = it["amount"] as? String ?: ""
        )
    } ?: emptyList()

    return Dish(
        id = id,
        categoryId = (this["categoryId"] as? Long)?.toInt() ?: 0,
        name = this["name"] as? String ?: "",
        minutesToCook = (this["minutesToCook"] as? Long)?.toInt() ?: 0,
        ingredients = ingredientsList,
        steps = this["steps"] as? String ?: "",
        calories = (this["calories"] as? Long)?.toInt() ?: 0,
        proteins = (this["proteins"] as? Long)?.toInt() ?: 0,
        fats = (this["fats"] as? Long)?.toInt() ?: 0,
        carbs = (this["carbs"] as? Long)?.toInt() ?: 0,
        imageUri = this["imageUri"] as? String,
        isCustom = this["isCustom"] as? Boolean ?: false
    )
}

// ConsumptionHistory to Firebase Map
fun ConsumptionHistory.toFirebaseMap(): Map<String, Any> {
    return mapOf(
        "datetime" to datetime,
        "foodId" to foodId,
        "isDish" to isDish,
        "name" to name,
        "grams" to grams,
        "calories" to calories,
        "proteins" to proteins,
        "fats" to fats,
        "carbs" to carbs
    )
}

// Firebase Map to ConsumptionHistory
fun Map<String, Any>.toConsumptionHistory(id: Int, userId: Int): ConsumptionHistory {
    return ConsumptionHistory(
        id = id,
        userId = userId,
        datetime = this["datetime"] as? Long ?: 0L,
        foodId = (this["foodId"] as? Long)?.toInt() ?: 0,
        isDish = this["isDish"] as? Boolean ?: false,
        name = this["name"] as? String ?: "",
        grams = (this["grams"] as? Long)?.toInt() ?: 0,
        calories = (this["calories"] as? Long)?.toInt() ?: 0,
        proteins = (this["proteins"] as? Long)?.toInt() ?: 0,
        fats = (this["fats"] as? Long)?.toInt() ?: 0,
        carbs = (this["carbs"] as? Long)?.toInt() ?: 0
    )
}

// DailyNutrition to Firebase Map
fun DailyNutrition.toFirebaseMap(): Map<String, Any> {
    return mapOf(
        "date" to date,
        "calories" to calories,
        "proteins" to proteins,
        "fats" to fats,
        "carbs" to carbs
    )
}