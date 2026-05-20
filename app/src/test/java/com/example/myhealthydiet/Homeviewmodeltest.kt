package com.example.myhealthydiet.ui.screens.home

import app.cash.turbine.test
import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.domain.models.DailyNutrition
import com.example.myhealthydiet.domain.models.User
import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal
import com.example.myhealthydiet.domain.usecases.history.GetTodayHistoryUseCase
import com.example.myhealthydiet.domain.usecases.nutrition.CheckAndResetDailyNutritionUseCase
import com.example.myhealthydiet.domain.usecases.nutrition.GetDailyNutritionUseCase
import com.example.myhealthydiet.domain.usecases.profile.GetUserProfileUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

/**
 * Тесты HomeViewModel.
 * Проверяем что UI-стейт правильно собирается из данных репозиториев.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val getDailyNutritionUseCase: GetDailyNutritionUseCase = mockk()
    private val getTodayHistoryUseCase: GetTodayHistoryUseCase = mockk()
    private val checkAndResetUseCase: CheckAndResetDailyNutritionUseCase = mockk(relaxed = true)

    private val testUser = User(
        id = 1, firebaseUid = "uid123", email = "test@test.com",
        age = 25, sex = Gender.MALE, weight = 80, height = 180,
        activityLevel = ActivityLevel.MODERATE, goal = Goal.MAINTAIN,
        calories = 2500, proteins = 150, fats = 80, carbs = 320,
        lastSyncTimestamp = null
    )

    private val testNutrition = DailyNutrition(
        userId = 1, date = "2026-04-01",
        calories = 2000, proteins = 120, fats = 60, carbs = 250
    )

    private val testHistory = listOf(
        ConsumptionHistory(
            id = 1, userId = 1, datetime = System.currentTimeMillis(),
            foodId = 1, isDish = false, name = "Куриная грудка",
            grams = 150, calories = 270, proteins = 34, fats = 27, carbs = 2
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `начальное состояние - isLoading = true`() = runTest {
        every { getUserProfileUseCase() } returns flowOf(testUser)
        every { getDailyNutritionUseCase() } returns flowOf(testNutrition)
        every { getTodayHistoryUseCase() } returns flowOf(testHistory)

        val viewModel = HomeViewModel(
            getUserProfileUseCase, getDailyNutritionUseCase,
            getTodayHistoryUseCase, checkAndResetUseCase
        )

        // Первое значение до загрузки данных
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `после загрузки - данные пользователя попадают в стейт`() = runTest {
        every { getUserProfileUseCase() } returns flowOf(testUser)
        every { getDailyNutritionUseCase() } returns flowOf(testNutrition)
        every { getTodayHistoryUseCase() } returns flowOf(testHistory)

        val viewModel = HomeViewModel(
            getUserProfileUseCase, getDailyNutritionUseCase,
            getTodayHistoryUseCase, checkAndResetUseCase
        )

        viewModel.uiState.test {
            // Пропускаем начальный loading
            skipItems(1)
            val state = awaitItem()
            assertEquals(testUser, state.user)
            assertEquals(testNutrition, state.dailyNutrition)
            assertEquals(testHistory, state.todayHistory)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `история пуста - todayHistory в стейте пустой список`() = runTest {
        every { getUserProfileUseCase() } returns flowOf(testUser)
        every { getDailyNutritionUseCase() } returns flowOf(testNutrition)
        every { getTodayHistoryUseCase() } returns flowOf(emptyList())

        val viewModel = HomeViewModel(
            getUserProfileUseCase, getDailyNutritionUseCase,
            getTodayHistoryUseCase, checkAndResetUseCase
        )

        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            assertEquals(emptyList<ConsumptionHistory>(), state.todayHistory)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `норма питания null - стейт содержит null для dailyNutrition`() = runTest {
        every { getUserProfileUseCase() } returns flowOf(testUser)
        every { getDailyNutritionUseCase() } returns flowOf(null)
        every { getTodayHistoryUseCase() } returns flowOf(emptyList())

        val viewModel = HomeViewModel(
            getUserProfileUseCase, getDailyNutritionUseCase,
            getTodayHistoryUseCase, checkAndResetUseCase
        )

        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            assertEquals(null, state.dailyNutrition)
            cancelAndIgnoreRemainingEvents()
        }
    }
}