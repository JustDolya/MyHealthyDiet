package com.example.myhealthydiet.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myhealthydiet.ui.components.BrandButton
import com.example.myhealthydiet.ui.components.BrandTextField
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // Заголовок
        Text(
            text = "MyHealthyDiet",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = BrandOrange,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Войдите в свой аккаунт",
            fontSize = 15.sp,
            color = TextSecondary,
        )

        Spacer(Modifier.height(40.dp))

        // Email
        BrandTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = "Электронная почта",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(Modifier.height(16.dp))

        // Пароль
        BrandTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = "Пароль",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
                        tint = BrandOrange,
                    )
                }
            },
        )

        // Ошибка
        if (uiState.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = uiState.error!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.height(32.dp))

        // Кнопка входа
        if (uiState.isLoading) {
            CircularProgressIndicator(
                color = BrandOrange,
                modifier = Modifier.size(48.dp),
            )
        } else {
            BrandButton(
                text = "Войти",
                onClick = viewModel::login,
            )
        }

        Spacer(Modifier.height(16.dp))

        // Переход к регистрации
        TextButton(onClick = onNavigateToRegister) {
            Text(
                text = "Нет аккаунта? Зарегистрироваться",
                color = BrandOrange,
                fontSize = 14.sp,
            )
        }
    }
}