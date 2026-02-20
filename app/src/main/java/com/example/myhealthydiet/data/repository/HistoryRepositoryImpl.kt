package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.local.room.dao.ConsumptionHistoryDao
import com.example.myhealthydiet.data.mappers.toDomain
import com.example.myhealthydiet.data.mappers.toEntity
import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: ConsumptionHistoryDao
) : HistoryRepository {

    override fun getHistoryByUser(userId: Int): Flow<List<ConsumptionHistory>> {
        return historyDao.getHistoryByUser(userId).map { list -> list.map { it.toDomain() } }
    }

    override fun getHistoryByDateRange(userId: Int, startTimestamp: Long, endTimestamp: Long): Flow<List<ConsumptionHistory>> {
        return historyDao.getHistoryByDateRange(userId, startTimestamp, endTimestamp)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getTodayHistory(userId: Int): Flow<List<ConsumptionHistory>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val todayEnd = calendar.timeInMillis

        return historyDao.getTodayHistory(userId, todayStart, todayEnd)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addConsumption(history: ConsumptionHistory): Long {
        return historyDao.insertHistory(history.toEntity())
    }

    override suspend fun deleteConsumption(history: ConsumptionHistory) {
        historyDao.deleteHistory(history.toEntity())
    }

    override suspend fun deleteOldHistory() {
        val threeWeeksAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(21)
        historyDao.deleteOldHistory(threeWeeksAgo)
    }

    override suspend fun shouldCleanHistory(): Boolean {
        // Проверяем, понедельник ли сегодня и прошло ли 3 недели с последней очистки
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // TODO: Добавить логику отслеживания последней очистки (можно в SharedPreferences)
        return dayOfWeek == Calendar.MONDAY
    }
}