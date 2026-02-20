package com.example.myhealthydiet.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_categories")
data class ProductCategoryEntity(
    @PrimaryKey val id: Int, // фиксированные ID (1-15)
    val name: String,
    val imagePath: String
)