package com.example.myhealthydiet.domain.models

data class ConsumptionHistory(
    val id: Int,
    val userId: Int,
    val datetime: Long,
    val foodId: Int,
    val isDish: Boolean,
    val name: String,
    val grams: Int,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int
)