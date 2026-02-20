package com.example.myhealthydiet.data.mappers

import com.example.myhealthydiet.data.local.room.entities.ConsumptionHistoryEntity
import com.example.myhealthydiet.domain.models.ConsumptionHistory

fun ConsumptionHistoryEntity.toDomain(): ConsumptionHistory {
    return ConsumptionHistory(
        id = id,
        userId = userId,
        datetime = datetime,
        foodId = foodId,
        isDish = isDish,
        name = name,
        grams = grams,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs
    )
}

fun ConsumptionHistory.toEntity(firebaseId: String? = null): ConsumptionHistoryEntity {
    return ConsumptionHistoryEntity(
        id = id,
        userId = userId,
        datetime = datetime,
        foodId = foodId,
        isDish = isDish,
        name = name,
        grams = grams,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs,
        firebaseId = firebaseId
    )
}