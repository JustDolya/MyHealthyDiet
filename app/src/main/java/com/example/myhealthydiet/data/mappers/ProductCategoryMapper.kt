package com.example.myhealthydiet.data.mappers

import com.example.myhealthydiet.data.local.room.entities.ProductCategoryEntity
import com.example.myhealthydiet.domain.models.ProductCategory

data class ProductCategory(
    val id: Int,
    val name: String,
    val imagePath: String
)

fun ProductCategoryEntity.toDomain(): ProductCategory {
    return ProductCategory(
        id = id,
        name = name,
        imagePath = imagePath
    )
}

fun ProductCategory.toEntity(): ProductCategoryEntity {
    return ProductCategoryEntity(
        id = id,
        name = name,
        imagePath = imagePath
    )
}