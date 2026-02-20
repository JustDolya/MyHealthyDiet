package com.example.myhealthydiet.domain.usecases.history

import com.example.myhealthydiet.domain.repository.HistoryRepository
import javax.inject.Inject

class CleanOldHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke() {
        if (historyRepository.shouldCleanHistory()) {
            historyRepository.deleteOldHistory()
        }
    }
}