package com.example.myhealthydiet.domain.usecases.history

import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class GetHistoryByPeriodUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(period: Period): Flow<List<ConsumptionHistory>> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, -period.days)
        val startTime = calendar.timeInMillis

        return historyRepository.getHistoryByDateRange(1, startTime, endTime)
    }

    enum class Period(val days: Int) {
        WEEK(7),
        TWO_WEEKS(14),
        THREE_WEEKS(21)
    }
}