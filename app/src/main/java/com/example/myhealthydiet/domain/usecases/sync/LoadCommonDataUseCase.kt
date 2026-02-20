package com.example.myhealthydiet.domain.usecases.sync

import com.example.myhealthydiet.domain.repository.SyncRepository
import javax.inject.Inject

class LoadCommonDataUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        // Загружает стандартные продукты и блюда из Firebase
        return syncRepository.loadCommonData()
    }
}