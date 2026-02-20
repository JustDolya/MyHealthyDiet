// data/mappers/ProductMapper.kt
package com.example.myhealthydiet.data.mappers

import com.example.myhealthydiet.data.local.room.entities.ProductEntity
import com.example.myhealthydiet.domain.models.Product

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        categoryId = categoryId,
        name = name,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs,
        isCustom = isCustom
    )
}

fun Product.toEntity(firebaseId: String? = null): ProductEntity {
    return ProductEntity(
        id = id,
        categoryId = categoryId,
        name = name,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs,
        isCustom = isCustom,
        firebaseId = firebaseId // ← теперь работает
    )
}