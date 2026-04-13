package com.example.myhealthydiet.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myhealthydiet.data.local.room.converters.DatabaseConverters
import com.example.myhealthydiet.data.local.room.dao.*
import com.example.myhealthydiet.data.local.room.entities.*

@Database(
    entities = [
        UserEntity::class,
        DailyNutritionEntity::class,
        ProductCategoryEntity::class,
        ProductEntity::class,
        DishCategoryEntity::class,
        DishEntity::class,
        ConsumptionHistoryEntity::class
    ],
    version = 2, // ← bumped: добавлен уникальный индекс в ConsumptionHistoryEntity
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun dailyNutritionDao(): DailyNutritionDao
    abstract fun productCategoryDao(): ProductCategoryDao
    abstract fun productDao(): ProductDao
    abstract fun dishCategoryDao(): DishCategoryDao
    abstract fun dishDao(): DishDao
    abstract fun consumptionHistoryDao(): ConsumptionHistoryDao

    companion object {
        const val DATABASE_NAME = "nutrition_tracker_db"
    }
}