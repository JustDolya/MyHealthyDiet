package com.example.myhealthydiet

import androidx.lifecycle.ViewModel
import com.example.myhealthydiet.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(authRepository.isUserLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun refreshAuthState() {
        _isLoggedIn.value = authRepository.isUserLoggedIn()
    }
}
