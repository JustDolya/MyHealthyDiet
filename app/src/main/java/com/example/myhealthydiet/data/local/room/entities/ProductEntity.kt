package com.example.myhealthydiet.data.local.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = ProductCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val name: String,
    val calories: Int, // на 100г
    val proteins: Int, // на 100г
    val fats: Int, // на 100г
    val carbs: Int, // на 100г
    val isCustom: Boolean = false, // true = создан пользователем
    val firebaseId: String? = null // ID в Firebase (для синхронизации)
)