package com.example.myhealthydiet.data.local.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dishes",
    foreignKeys = [
        ForeignKey(
            entity = DishCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class DishEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val name: String,
    val minutesToCook: Int,
    val ingredients: String, // JSON
    val steps: String,
    val calories: Int, // на всё блюдо
    val proteins: Int,
    val fats: Int,
    val carbs: Int,
    val imageUri: String?,
    val isCustom: Boolean = false, // true = создан пользователем
    val firebaseId: String? = null // ID в Firebase
)