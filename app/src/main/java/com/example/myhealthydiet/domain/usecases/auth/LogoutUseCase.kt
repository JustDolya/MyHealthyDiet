package com.example.myhealthydiet.domain.usecases.auth

import com.example.myhealthydiet.domain.repository.AuthRepository
import com.example.myhealthydiet.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            // 1. Выход из Firebase Auth
            authRepository.logout()

            // 2. Очистка локальной БД
            userRepository.deleteUser() // CASCADE удалит все связанные данные

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}