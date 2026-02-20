package com.example.myhealthydiet.data.mappers

import com.example.myhealthydiet.data.local.room.entities.DishEntity
import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.domain.models.IngredientItem // ← изменили импорт
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()

fun DishEntity.toDomain(): Dish {
    // ← изменили TypeToken на IngredientItem
    val ingredientsType = object : TypeToken<List<IngredientItem>>() {}.type
    val ingredientsList: List<IngredientItem> = try {
        gson.fromJson(ingredients, ingredientsType)
    } catch (e: Exception) {
        emptyList()
    }

    return Dish(
        id = id,
        categoryId = categoryId,
        name = name,
        minutesToCook = minutesToCook,
        ingredients = ingredientsList,
        steps = steps,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs,
        imageUri = imageUri,
        isCustom = isCustom
    )
}

fun Dish.toEntity(firebaseId: String? = null): DishEntity {
    val ingredientsJson = gson.toJson(ingredients)

    return DishEntity(
        id = id,
        categoryId = categoryId,
        name = name,
        minutesToCook = minutesToCook,
        ingredients = ingredientsJson,
        steps = steps,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs,
        imageUri = imageUri,
        isCustom = isCustom,
        firebaseId = firebaseId
    )
}