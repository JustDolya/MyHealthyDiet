package com.example.myhealthydiet.data.local.room.converters

import androidx.room.TypeConverter
import java.util.Date

class DatabaseConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}