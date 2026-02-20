package com.example.myhealthydiet.domain.models

import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal

data class User(
    val id: Int,
    val firebaseUid: String,
    val email: String,
    val age: Int,
    val sex: Gender,
    val weight: Int,
    val height: Int,
    val activityLevel: ActivityLevel,
    val goal: Goal,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int,
    val lastSyncTimestamp: Long?
)