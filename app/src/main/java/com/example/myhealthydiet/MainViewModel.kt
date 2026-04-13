package com.example.myhealthydiet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthydiet.domain.repository.AuthRepository
import com.example.myhealthydiet.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        viewModelScope.launch {
            // Firebase может помнить сессию после переустановки —
            // считаем залогиненным только если есть и Firebase-токен И локальный пользователь
            val firebaseLoggedIn = authRepository.isUserLoggedIn()
            val localUser = userRepository.getUserOnce()
            _isLoggedIn.value = firebaseLoggedIn && localUser != null
        }
    }
}