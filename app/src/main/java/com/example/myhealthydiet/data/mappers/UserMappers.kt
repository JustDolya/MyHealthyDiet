package com.example.myhealthydiet.data.mappers

import com.example.myhealthydiet.data.local.room.entities.UserEntity
import com.example.myhealthydiet.domain.models.*
import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        firebaseUid = firebaseUid,
        email = email,
        age = age,
        sex = Gender.fromInt(sex),
        weight = weight,
        height = height,
        activityLevel = ActivityLevel.fromInt(activityLevel),
        goal = Goal.fromInt(goal),
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs,
        lastSyncTimestamp = lastSyncTimestamp
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        firebaseUid = firebaseUid,
        email = email,
        age = age,
        sex = sex.value,
        weight = weight,
        height = height,
        activityLevel = activityLevel.value,
        goal = goal.value,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs,
        lastSyncTimestamp = lastSyncTimestamp
    )
}