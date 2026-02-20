package com.example.myhealthydiet.data.local.room.dao

import androidx.room.*
import com.example.myhealthydiet.data.local.room.entities.ConsumptionHistoryEntity
import kotlinx.coroutines.flow.Flow

// data/local/room/dao/ConsumptionHistoryDao.kt
@Dao
interface ConsumptionHistoryDao {

    @Query("SELECT * FROM consumption_history WHERE userId = :userId ORDER BY datetime DESC")
    fun getHistoryByUser(userId: Int): Flow<List<ConsumptionHistoryEntity>>

    @Query("""
        SELECT * FROM consumption_history 
        WHERE userId = :userId AND datetime >= :startTimestamp AND datetime <= :endTimestamp
        ORDER BY datetime DESC
    """)
    fun getHistoryByDateRange(userId: Int, startTimestamp: Long, endTimestamp: Long): Flow<List<ConsumptionHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: ConsumptionHistoryEntity): Long

    @Delete
    suspend fun deleteHistory(history: ConsumptionHistoryEntity)

    // Очистка истории старше 21 дня (вызывается каждый понедельник раз в 3 недели)
    @Query("DELETE FROM consumption_history WHERE datetime < :threeWeeksAgo")
    suspend fun deleteOldHistory(threeWeeksAgo: Long)

    @Query("""
        SELECT SUM(calories) as totalCalories, 
               SUM(proteins) as totalProteins, 
               SUM(fats) as totalFats, 
               SUM(carbs) as totalCarbs
        FROM consumption_history 
        WHERE userId = :userId AND datetime >= :startTimestamp AND datetime < :endTimestamp
    """)
    suspend fun getDailyTotals(userId: Int, startTimestamp: Long, endTimestamp: Long): DailyTotals?
}