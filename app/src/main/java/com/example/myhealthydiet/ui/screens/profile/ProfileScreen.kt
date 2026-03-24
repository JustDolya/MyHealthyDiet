package com.example.myhealthydiet.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myhealthydiet.domain.models.enums.ActivityLevel
import com.example.myhealthydiet.domain.models.enums.Gender
import com.example.myhealthydiet.domain.models.enums.Goal
import com.example.myhealthydiet.ui.components.BrandButton
import com.example.myhealthydiet.ui.components.BrandTextField
import com.example.myhealthydiet.ui.components.SectionTitle
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.TextSecondary
import androidx.compose.material3.Scaffold

@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Редирект после выхода
    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) onLogout()
    }

    // Snackbar для ошибок и успехов
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onErrorDismiss()
        }
    }
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onSuccessDismiss()
        }
    }

    // Диалог подтверждения выхода
    if (uiState.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = viewModel::onDismissLogoutDialog,
            title = { Text("Выйти из аккаунта?") },
            text = {
                Text(
                    "Все несинхронизированные данные будут удалены с устройства.",
                    color = TextSecondary,
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::logout) {
                    Text("Выйти", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDismissLogoutDialog) {
                    Text("Отмена", color = BrandOrange)
                }
            },
        )
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BrandOrange)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            // ── Заголовок с почтой ────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = BrandOrange,
                    modifier = Modifier.size(32.dp),
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Профиль", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Black)
                    Text(
                        text = uiState.user?.email ?: "",
                        fontSize = 13.sp,
                        color = TextSecondary,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Общая информация ──────────────────────────────────────────────
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
                                onClick = { viewModel.onGenderChange(gender) },
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
                    onValueChange = viewModel::onAgeChange,
                    label = "Возраст",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                )
                BrandTextField(
                    value = uiState.height,
                    onValueChange = viewModel::onHeightChange,
                    label = "Рост, см",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                )
                BrandTextField(
                    value = uiState.weight,
                    onValueChange = viewModel::onWeightChange,
                    label = "Вес, кг",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Дневная активность ────────────────────────────────────────────
            SectionTitle("● Дневная активность:")
            Spacer(Modifier.height(12.dp))

            Slider(
                value = uiState.activityLevel.value.toFloat(),
                onValueChange = { viewModel.onActivityLevelChange(ActivityLevel.fromInt(it.toInt())) },
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

            // ── Цель ─────────────────────────────────────────────────────────
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
                                onClick = { viewModel.onGoalChange(goal) },
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

            Spacer(Modifier.height(32.dp))

            // ── Кнопка сохранения ─────────────────────────────────────────────
            if (uiState.isSaving) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(color = BrandOrange, modifier = Modifier.size(48.dp))
                }
            } else {
                BrandButton(text = "Сохранить и пересчитать КБЖУ", onClick = viewModel::saveProfile)
            }

            Spacer(Modifier.height(12.dp))

            // ── Кнопка синхронизации ──────────────────────────────────────────
            if (uiState.isSyncing) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(color = BrandOrange, modifier = Modifier.size(48.dp))
                }
            } else {
                TextButton(
                    onClick = viewModel::sync,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Filled.CloudSync, contentDescription = null, tint = BrandOrange)
                    Spacer(Modifier.width(8.dp))
                    Text("Синхронизировать с облаком", color = BrandOrange, fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Кнопка выхода ─────────────────────────────────────────────────
            TextButton(
                onClick = viewModel::onShowLogoutDialog,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.width(8.dp))
                Text("Выйти из аккаунта", color = MaterialTheme.colorScheme.error, fontSize = 15.sp)
            }
        }
    }
}