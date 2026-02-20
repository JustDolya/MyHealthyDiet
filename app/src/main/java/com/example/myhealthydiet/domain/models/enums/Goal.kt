package com.example.myhealthydiet.domain.models.enums

enum class Goal(val value: Int, val displayName: String) {
    LOSE_WEIGHT(0, "Похудение"),
    MAINTAIN(1, "Поддержание веса"),
    GAIN_WEIGHT(2, "Набор веса");

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}