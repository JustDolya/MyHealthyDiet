package com.example.myhealthydiet.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int = 1, // всегда 1 локально
    val firebaseUid: String, // Firebase UID
    val email: String,
    val age: Int,
    val sex: Int, // GenderEnum
    val weight: Int,
    val height: Int,
    val activityLevel: Int, // ActivityLevelEnum
    val goal: Int, // GoalEnum
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int,
    val lastSyncTimestamp: Long? = null // timestamp последней синхронизации
)