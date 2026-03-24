package com.example.myhealthydiet.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.domain.models.DailyNutrition
import com.example.myhealthydiet.domain.models.User
import com.example.myhealthydiet.domain.usecases.history.GetTodayHistoryUseCase
import com.example.myhealthydiet.domain.usecases.nutrition.CheckAndResetDailyNutritionUseCase
import com.example.myhealthydiet.domain.usecases.nutrition.GetDailyNutritionUseCase
import com.example.myhealthydiet.domain.usecases.profile.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val user: User? = null,
    val dailyNutrition: DailyNutrition? = null,
    val todayHistory: List<ConsumptionHistory> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getDailyNutritionUseCase: GetDailyNutritionUseCase,
    private val getTodayHistoryUseCase: GetTodayHistoryUseCase,
    private val checkAndResetDailyNutritionUseCase: CheckAndResetDailyNutritionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            checkAndResetDailyNutritionUseCase()
        }
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                getUserProfileUseCase(),
                getDailyNutritionUseCase(),
                getTodayHistoryUseCase(),
            ) { user, nutrition, history ->
                HomeUiState(
                    user = user,
                    dailyNutrition = nutrition,
                    todayHistory = history,
                    isLoading = false,
                )
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }
}