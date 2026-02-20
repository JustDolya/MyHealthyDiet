package com.example.myhealthydiet.domain.usecases.history

import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): Flow<List<ConsumptionHistory>> {
        return historyRepository.getTodayHistory(userId = 1)
    }
}