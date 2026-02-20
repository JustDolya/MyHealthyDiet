package com.example.myhealthydiet.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun registerWithEmail(email: String, password: String): Result<FirebaseUser>
    suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser>
    fun logout()
    fun getCurrentUser(): FirebaseUser?
    fun isUserLoggedIn(): Boolean
}