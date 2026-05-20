package com.example.myhealthydiet.domain.usecases.auth

import com.example.myhealthydiet.domain.repository.AuthRepository
import com.example.myhealthydiet.domain.repository.SyncRepository
import com.example.myhealthydiet.domain.usecases.init.InitializeAppUseCase
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Тесты логики входа в аккаунт.
 */
class LoginUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private val syncRepository: SyncRepository = mockk(relaxed = true)
    private val initializeAppUseCase: InitializeAppUseCase = mockk(relaxed = true)
    private val firebaseUser: FirebaseUser = mockk()

    private lateinit var useCase: LoginUseCase

    @Before
    fun setUp() {
        useCase = LoginUseCase(authRepository, syncRepository, initializeAppUseCase)
    }

    @Test
    fun `успешный логин - возвращает успех`() = runTest {
        coEvery { authRepository.loginWithEmail(any(), any()) } returns Result.success(firebaseUser)

        val result = useCase("test@example.com", "password123")

        assertTrue("Должен вернуть успех", result.isSuccess)
    }

    @Test
    fun `успешный логин - вызывает инициализацию данных`() = runTest {
        coEvery { authRepository.loginWithEmail(any(), any()) } returns Result.success(firebaseUser)

        useCase("test@example.com", "password123")

        coVerify { initializeAppUseCase() }
    }

    @Test
    fun `успешный логин - вызывает загрузку данных из облака`() = runTest {
        coEvery { authRepository.loginWithEmail(any(), any()) } returns Result.success(firebaseUser)

        useCase("test@example.com", "password123")

        coVerify { syncRepository.downloadAllData() }
    }

    @Test
    fun `ошибка Firebase - возвращает неуспех и не вызывает синхронизацию`() = runTest {
        coEvery { authRepository.loginWithEmail(any(), any()) } returns
                Result.failure(Exception("Неверный пароль"))

        val result = useCase("test@example.com", "wrongpassword")

        assertTrue("Должен вернуть ошибку", result.isFailure)
        coVerify(exactly = 0) { syncRepository.downloadAllData() }
        coVerify(exactly = 0) { initializeAppUseCase() }
    }

    @Test
    fun `ошибка Firebase - сообщение об ошибке передаётся наружу`() = runTest {
        coEvery { authRepository.loginWithEmail(any(), any()) } returns
                Result.failure(Exception("Пользователь не найден"))

        val result = useCase("unknown@example.com", "password123")

        assertTrue(result.exceptionOrNull()?.message?.contains("не найден") == true)
    }
}