package com.example.myhealthydiet.data.local.room.dao

import androidx.room.*
import com.example.myhealthydiet.data.local.room.entities.ConsumptionHistoryEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("""
        SELECT * FROM consumption_history
        WHERE userId = :userId AND datetime >= :startTimestamp AND datetime < :endTimestamp
        ORDER BY datetime DESC
    """)
    fun getTodayHistory(userId: Int, startTimestamp: Long, endTimestamp: Long): Flow<List<ConsumptionHistoryEntity>>

    // REPLACE — для новых записей пользователя (добавление в рацион)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: ConsumptionHistoryEntity): Long

    // IGNORE — для загрузки из облака, не перезаписывает существующие
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistoryIgnore(history: ConsumptionHistoryEntity): Long

    @Delete
    suspend fun deleteHistory(history: ConsumptionHistoryEntity)

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