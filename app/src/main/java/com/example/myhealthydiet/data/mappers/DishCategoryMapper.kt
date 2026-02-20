package com.example.myhealthydiet.data.mappers

import com.example.myhealthydiet.data.local.room.entities.DishCategoryEntity
import com.example.myhealthydiet.domain.models.DishCategory

data class DishCategory(
    val id: Int,
    val name: String,
    val imagePath: String
)

fun DishCategoryEntity.toDomain(): DishCategory {
    return DishCategory(
        id = id,
        name = name,
        imagePath = imagePath
    )
}

fun DishCategory.toEntity(): DishCategoryEntity {
    return DishCategoryEntity(
        id = id,
        name = name,
        imagePath = imagePath
    )
}