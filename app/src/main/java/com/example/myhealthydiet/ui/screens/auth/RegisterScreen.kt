package com.example.myhealthydiet.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal
import com.example.myhealthydiet.ui.components.BrandButton
import com.example.myhealthydiet.ui.components.BrandTextField
import com.example.myhealthydiet.ui.components.SectionTitle
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.TextSecondary

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onRegisterSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        // Кнопка назад
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    if (uiState.step == RegisterStep.ACCOUNT) onNavigateBack()
                    else viewModel.prevStep()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = BrandOrange,
                )
            }
            Text(
                text = if (uiState.step == RegisterStep.ACCOUNT) "Регистрация" else "Ваши параметры",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BrandOrange,
            )
        }

        Text(
            text = if (uiState.step == RegisterStep.ACCOUNT) "Шаг 1 из 2" else "Шаг 2 из 2",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(start = 48.dp),
        )

        Spacer(Modifier.height(24.dp))

        when (uiState.step) {
            RegisterStep.ACCOUNT -> AccountStep(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onPasswordConfirmChange = viewModel::onPasswordConfirmChange,
                onNext = viewModel::nextStep,
                onBack = onNavigateBack,
            )
            RegisterStep.PROFILE -> ProfileStep(
                uiState = uiState,
                onGenderChange = viewModel::onGenderChange,
                onAgeChange = viewModel::onAgeChange,
                onHeightChange = viewModel::onHeightChange,
                onWeightChange = viewModel::onWeightChange,
                onActivityLevelChange = viewModel::onActivityLevelChange,
                onGoalChange = viewModel::onGoalChange,
                onRegister = viewModel::register,
            )
        }
    }
}

// ─── Шаг 1: Email + пароль ───────────────────────────────────────────────────

@Composable
private fun AccountStep(
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordConfirmVisible by remember { mutableStateOf(false) }

    BrandTextField(
        value = uiState.email,
        onValueChange = onEmailChange,
        label = "Электронная почта",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
    )
    Spacer(Modifier.height(16.dp))

    BrandTextField(
        value = uiState.password,
        onValueChange = onPasswordChange,
        label = "Пароль",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    tint = BrandOrange,
                )
            }
        },
    )
    Spacer(Modifier.height(16.dp))

    BrandTextField(
        value = uiState.passwordConfirm,
        onValueChange = onPasswordConfirmChange,
        label = "Подтвердите пароль",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        visualTransformation = if (passwordConfirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordConfirmVisible = !passwordConfirmVisible }) {
                Icon(
                    imageVector = if (passwordConfirmVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    tint = BrandOrange,
                )
            }
        },
    )

    ErrorText(uiState.error)
    Spacer(Modifier.height(32.dp))
    BrandButton(text = "Далее", onClick = onNext)
    Spacer(Modifier.height(12.dp))
    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
        Text("Уже есть аккаунт? Войти", color = BrandOrange, fontSize = 14.sp)
    }
}

// ─── Шаг 2: Физические параметры ─────────────────────────────────────────────

@Composable
private fun ProfileStep(
    uiState: RegisterUiState,
    onGenderChange: (Gender) -> Unit,
    onAgeChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onActivityLevelChange: (ActivityLevel) -> Unit,
    onGoalChange: (Goal) -> Unit,
    onRegister: () -> Unit,
) {
    // ── Общая информация ──────────────────────────────────────────────────────
    SectionTitle("● Общая информация:")
    Spacer(Modifier.height(12.dp))

    // Пол
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        listOf(Gender.FEMALE to "Женщина", Gender.MALE to "Мужчина").forEach { (gender, label) ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = uiState.gender == gender,
                        onClick = { onGenderChange(gender) },
                        role = Role.RadioButton,
                    )
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = uiState.gender == gender,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = BrandOrange),
                )
                Spacer(Modifier.width(4.dp))
                Text(label)
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    // Возраст / Рост / Вес
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BrandTextField(
            value = uiState.age,
            onValueChange = onAgeChange,
            label = "Возраст",
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        )
        BrandTextField(
            value = uiState.height,
            onValueChange = onHeightChange,
            label = "Рост, см",
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        )
        BrandTextField(
            value = uiState.weight,
            onValueChange = onWeightChange,
            label = "Вес, кг",
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        )
    }

    Spacer(Modifier.height(24.dp))

    // ── Дневная активность ────────────────────────────────────────────────────
    SectionTitle("● Дневная активность:")
    Spacer(Modifier.height(12.dp))

    val activityLevels = ActivityLevel.entries
    Slider(
        value = uiState.activityLevel.value.toFloat(),
        onValueChange = { onActivityLevelChange(ActivityLevel.fromInt(it.toInt())) },
        valueRange = 0f..4f,
        steps = 3,
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = BrandOrange,
            activeTrackColor = BrandOrange,
            inactiveTrackColor = BrandOrange.copy(alpha = 0.3f),
            activeTickColor = BrandOrange,
            inactiveTickColor = BrandOrange.copy(alpha = 0.3f),
        ),
    )

    val activityDescriptions = listOf(
        "Сидячий образ жизни\nМинимум движения, офисная работа",
        "Лёгкая активность\nПешие прогулки 1–3 раза в неделю",
        "Средняя\nЕжедневно гуляю не меньше часа",
        "Высокая активность\nИнтенсивные тренировки 4–5 раз в неделю",
        "Очень высокая\nФизический труд или ежедневные тренировки",
    )
    Text(
        text = activityDescriptions[uiState.activityLevel.value],
        fontSize = 14.sp,
        color = TextSecondary,
        modifier = Modifier.padding(horizontal = 4.dp),
    )

    Spacer(Modifier.height(24.dp))

    // ── Ваша цель ─────────────────────────────────────────────────────────────
    SectionTitle("● Ваша цель:")
    Spacer(Modifier.height(12.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        listOf(
            Goal.LOSE_WEIGHT to "Сбросить\nвес",
            Goal.MAINTAIN to "Поддерживать\nвес",
            Goal.GAIN_WEIGHT to "Набрать\nвес",
        ).forEach { (goal, label) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .selectable(
                        selected = uiState.goal == goal,
                        onClick = { onGoalChange(goal) },
                        role = Role.RadioButton,
                    )
                    .weight(1f),
            ) {
                RadioButton(
                    selected = uiState.goal == goal,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = BrandOrange),
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = if (uiState.goal == goal) BrandOrange else TextSecondary,
                )
            }
        }
    }

    ErrorText(uiState.error)
    Spacer(Modifier.height(32.dp))

    if (uiState.isLoading) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            CircularProgressIndicator(color = BrandOrange, modifier = Modifier.size(48.dp))
        }
    } else {
        BrandButton(text = "Рассчитать и зарегистрироваться", onClick = onRegister)
    }
}

@Composable
private fun ErrorText(error: String?) {
    if (error != null) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}