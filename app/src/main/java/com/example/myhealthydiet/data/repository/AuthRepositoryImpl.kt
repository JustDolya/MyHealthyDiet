package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.remote.firebase.auth.FirebaseAuthDataSource
import com.example.myhealthydiet.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun registerWithEmail(email: String, password: String): Result<FirebaseUser> {
        return firebaseAuthDataSource.registerWithEmail(email, password)
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser> {
        return firebaseAuthDataSource.loginWithEmail(email, password)
    }

    override fun logout() {
        firebaseAuthDataSource.logout()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuthDataSource.getCurrentUser()
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuthDataSource.isUserLoggedIn()
    }
}