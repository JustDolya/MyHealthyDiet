package com.example.myhealthydiet.domain.repository

import com.example.myhealthydiet.domain.models.ConsumptionHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistoryByUser(userId: Int): Flow<List<ConsumptionHistory>>
    fun getHistoryByDateRange(userId: Int, startTimestamp: Long, endTimestamp: Long): Flow<List<ConsumptionHistory>>
    fun getTodayHistory(userId: Int): Flow<List<ConsumptionHistory>>
    suspend fun addConsumption(history: ConsumptionHistory): Long
    suspend fun deleteConsumption(history: ConsumptionHistory)
    suspend fun deleteOldHistory()
    suspend fun shouldCleanHistory(): Boolean
}