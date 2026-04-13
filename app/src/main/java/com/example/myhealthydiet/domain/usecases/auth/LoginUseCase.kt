package com.example.myhealthydiet.domain.usecases.auth

import com.example.myhealthydiet.domain.repository.AuthRepository
import com.example.myhealthydiet.domain.repository.SyncRepository
import com.example.myhealthydiet.domain.usecases.init.InitializeAppUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val syncRepository: SyncRepository,
    private val initializeAppUseCase: InitializeAppUseCase,
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return try {
            // 1. Логин в Firebase
            val result = authRepository.loginWithEmail(email, password)
            if (result.isFailure) {
                return Result.failure(result.exceptionOrNull()!!)
            }

            // 2. Инициализируем стандартные данные (категории, продукты, блюда)
            //    isDataInitialized() внутри проверяет флаг — повторно не загрузит
            initializeAppUseCase()

            // 3. Загружаем данные пользователя из облака
            syncRepository.downloadAllData()

            // 4. Проверяем актуальность данных (multi-device sync)
            syncRepository.checkAndSyncIfNeeded()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}