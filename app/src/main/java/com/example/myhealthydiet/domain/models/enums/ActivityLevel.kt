package com.example.myhealthydiet.domain.models.enums

enum class ActivityLevel(val value: Int, val displayName: String) {
    SEDENTARY(0, "Сидячий образ жизни"),
    LIGHT(1, "Легкая активность"),
    MODERATE(2, "Умеренная активность"),
    ACTIVE(3, "Высокая активность"),
    VERY_ACTIVE(4, "Очень высокая активность");

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}