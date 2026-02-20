package com.example.myhealthydiet.domain.usecases.auth

import com.example.myhealthydiet.domain.models.User
import com.example.myhealthydiet.domain.repository.AuthRepository
import com.example.myhealthydiet.domain.repository.SyncRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val syncRepository: SyncRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return try {
            // 1. Логин в Firebase
            val result = authRepository.loginWithEmail(email, password)
            if (result.isFailure) {
                return Result.failure(result.exceptionOrNull()!!)
            }

            // 2. Загружаем данные пользователя из облака
            syncRepository.downloadAllData()

            // 3. Проверяем актуальность данных
            syncRepository.checkAndSyncIfNeeded()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}