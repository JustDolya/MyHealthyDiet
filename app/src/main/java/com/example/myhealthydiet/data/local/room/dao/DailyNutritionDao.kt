package com.example.myhealthydiet.data.local.room.dao

import androidx.room.*
import com.example.myhealthydiet.data.local.room.entities.DailyNutritionEntity
import kotlinx.coroutines.flow.Flow

// data/local/room/dao/DailyNutritionDao.kt
@Dao
interface DailyNutritionDao {

    @Query("SELECT * FROM daily_nutrition_remaining WHERE userId = :userId AND date = :date")
    fun getDailyNutrition(userId: Int, date: String): Flow<DailyNutritionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyNutrition(nutrition: DailyNutritionEntity)

    @Update
    suspend fun updateDailyNutrition(nutrition: DailyNutritionEntity)

    @Query("DELETE FROM daily_nutrition_remaining WHERE date < :oldestDate")
    suspend fun deleteOldRecords(oldestDate: String)

    // Обновление после добавления еды
    @Query("""
        UPDATE daily_nutrition_remaining 
        SET calories = calories - :calories,
            proteins = proteins - :proteins,
            fats = fats - :fats,
            carbs = carbs - :carbs
        WHERE userId = :userId AND date = :date
    """)
    suspend fun subtractNutrition(userId: Int, date: String, calories: Int, proteins: Int, fats: Int, carbs: Int)
}