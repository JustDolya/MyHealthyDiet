package com.example.myhealthydiet.domain.models.enums

// domain/models/enums/Gender.kt
enum class Gender(val value: Int) {
    MALE(0),
    FEMALE(1);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}