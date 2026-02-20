package com.example.myhealthydiet.domain.usecases.sync

import com.example.myhealthydiet.domain.repository.SyncRepository
import javax.inject.Inject

class CheckSyncOnStartupUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        // Проверяет, обновлены ли данные в облаке с другого устройства
        // Если да - загружает их
        return syncRepository.checkAndSyncIfNeeded()
    }
}