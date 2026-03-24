package com.example.myhealthydiet.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthydiet.domain.models.User
import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal
import com.example.myhealthydiet.domain.usecases.auth.LogoutUseCase
import com.example.myhealthydiet.domain.usecases.profile.GetUserProfileUseCase
import com.example.myhealthydiet.domain.usecases.profile.UpdateUserProfileUseCase
import com.example.myhealthydiet.domain.usecases.sync.SyncDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val age: String = "",
    val height: String = "",
    val weight: String = "",
    val gender: Gender = Gender.MALE,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val goal: Goal = Goal.MAINTAIN,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val isLoggedOut: Boolean = false,
    val showLogoutDialog: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val syncDataUseCase: SyncDataUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            getUserProfileUseCase().collect { user ->
                if (user != null && _uiState.value.isLoading) {
                    _uiState.update {
                        it.copy(
                            user = user,
                            age = user.age.toString(),
                            height = user.height.toString(),
                            weight = user.weight.toString(),
                            gender = user.sex,
                            activityLevel = user.activityLevel,
                            goal = user.goal,
                            isLoading = false,
                        )
                    }
                } else {
                    _uiState.update { it.copy(user = user) }
                }
            }
        }
    }

    fun onAgeChange(v: String) = _uiState.update { it.copy(age = v, error = null) }
    fun onHeightChange(v: String) = _uiState.update { it.copy(height = v, error = null) }
    fun onWeightChange(v: String) = _uiState.update { it.copy(weight = v, error = null) }
    fun onGenderChange(v: Gender) = _uiState.update { it.copy(gender = v) }
    fun onActivityLevelChange(v: ActivityLevel) = _uiState.update { it.copy(activityLevel = v) }
    fun onGoalChange(v: Goal) = _uiState.update { it.copy(goal = v) }
    fun onErrorDismiss() = _uiState.update { it.copy(error = null) }
    fun onSuccessDismiss() = _uiState.update { it.copy(successMessage = null) }
    fun onShowLogoutDialog() = _uiState.update { it.copy(showLogoutDialog = true) }
    fun onDismissLogoutDialog() = _uiState.update { it.copy(showLogoutDialog = false) }

    fun saveProfile() {
        val s = _uiState.value
        val user = s.user ?: return
        val age = s.age.toIntOrNull()
        val height = s.height.toIntOrNull()
        val weight = s.weight.toIntOrNull()
        when {
            age == null || height == null || weight == null ->
                _uiState.update { it.copy(error = "Заполните все поля") }
            age !in 10..120 ->
                _uiState.update { it.copy(error = "Некорректный возраст (10–120)") }
            height !in 100..250 ->
                _uiState.update { it.copy(error = "Некорректный рост (100–250 см)") }
            weight !in 30..300 ->
                _uiState.update { it.copy(error = "Некорректный вес (30–300 кг)") }
            else -> viewModelScope.launch {
                _uiState.update { it.copy(isSaving = true, error = null) }
                val updated = user.copy(
                    age = age, height = height, weight = weight,
                    sex = s.gender, activityLevel = s.activityLevel, goal = s.goal,
                )
                val result = updateUserProfileUseCase(updated, recalculateNutrition = true)
                _uiState.update {
                    if (result.isSuccess)
                        it.copy(isSaving = false, successMessage = "Профиль обновлён, КБЖУ пересчитан")
                    else
                        it.copy(isSaving = false, error = result.exceptionOrNull()?.message ?: "Ошибка сохранения")
                }
            }
        }
    }

    fun sync() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, error = null) }
            val result = syncDataUseCase()
            _uiState.update {
                if (result.isSuccess)
                    it.copy(isSyncing = false, successMessage = "Данные синхронизированы с облаком")
                else
                    it.copy(isSyncing = false, error = result.exceptionOrNull()?.message ?: "Ошибка синхронизации")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.update { it.copy(isLoggedOut = true, showLogoutDialog = false) }
        }
    }
}