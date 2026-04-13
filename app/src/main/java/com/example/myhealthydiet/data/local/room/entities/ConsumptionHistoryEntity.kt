package com.example.myhealthydiet.data.local.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "consumption_history",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Добавлен уникальный индекс — предотвращает дубликаты при загрузке из облака
    indices = [
        Index(value = ["userId"]),
        Index(value = ["datetime"]),
        Index(value = ["userId", "datetime", "foodId", "isDish"], unique = true)
    ]
)
data class ConsumptionHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val datetime: Long,
    val foodId: Int,
    val isDish: Boolean,
    val name: String,
    val grams: Int,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int,
    val firebaseId: String? = null
)