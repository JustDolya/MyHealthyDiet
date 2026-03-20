package com.example.myhealthydiet.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal
import com.example.myhealthydiet.domain.usecases.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class RegisterStep { ACCOUNT, PROFILE }

data class RegisterUiState(
    val step: RegisterStep = RegisterStep.ACCOUNT,

    // Шаг 1 — аккаунт
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",

    // Шаг 2 — профиль
    val gender: Gender = Gender.MALE,
    val age: String = "",
    val height: String = "",
    val weight: String = "",
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val goal: Goal = Goal.MAINTAIN,

    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, error = null) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, error = null) }
    fun onPasswordConfirmChange(v: String) = _uiState.update { it.copy(passwordConfirm = v, error = null) }
    fun onGenderChange(v: Gender) = _uiState.update { it.copy(gender = v) }
    fun onAgeChange(v: String) = _uiState.update { it.copy(age = v, error = null) }
    fun onHeightChange(v: String) = _uiState.update { it.copy(height = v, error = null) }
    fun onWeightChange(v: String) = _uiState.update { it.copy(weight = v, error = null) }
    fun onActivityLevelChange(v: ActivityLevel) = _uiState.update { it.copy(activityLevel = v) }
    fun onGoalChange(v: Goal) = _uiState.update { it.copy(goal = v) }

    fun nextStep() {
        val s = _uiState.value
        when {
            s.email.isBlank() || s.password.isBlank() || s.passwordConfirm.isBlank() ->
                _uiState.update { it.copy(error = "Заполните все поля") }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches() ->
                _uiState.update { it.copy(error = "Введите корректный email") }
            s.password.length < 6 ->
                _uiState.update { it.copy(error = "Пароль должен содержать не менее 6 символов") }
            s.password != s.passwordConfirm ->
                _uiState.update { it.copy(error = "Пароли не совпадают") }
            else ->
                _uiState.update { it.copy(step = RegisterStep.PROFILE, error = null) }
        }
    }

    fun prevStep() = _uiState.update { it.copy(step = RegisterStep.ACCOUNT, error = null) }

    fun register() {
        val s = _uiState.value
        val age = s.age.toIntOrNull()
        val height = s.height.toIntOrNull()
        val weight = s.weight.toIntOrNull()

        when {
            age == null || height == null || weight == null ->
                _uiState.update { it.copy(error = "Заполните все поля") }
            age !in 10..120 ->
                _uiState.update { it.copy(error = "Введите корректный возраст (10–120)") }
            height !in 100..250 ->
                _uiState.update { it.copy(error = "Введите корректный рост (100–250 см)") }
            weight !in 30..300 ->
                _uiState.update { it.copy(error = "Введите корректный вес (30–300 кг)") }
            else -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                    val result = registerUseCase(
                        email = s.email.trim(),
                        password = s.password,
                        age = age,
                        sex = s.gender,
                        weight = weight,
                        height = height,
                        activityLevel = s.activityLevel,
                        goal = s.goal,
                    )
                    if (result.isSuccess) {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exceptionOrNull()?.message ?: "Ошибка регистрации"
                            )
                        }
                    }
                }
            }
        }
    }
}