package com.example.myhealthydiet.domain.usecases.sync

import com.example.myhealthydiet.domain.repository.SyncRepository
import javax.inject.Inject

class SyncDataUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        // Загружает локальные данные в Firebase
        return syncRepository.uploadAllData()
    }
}