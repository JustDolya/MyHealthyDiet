package com.example.myhealthydiet.domain.repository

import com.example.myhealthydiet.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User?>
    suspend fun getUserOnce(): User?
    suspend fun saveUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser()
    suspend fun updateSyncTimestamp(timestamp: Long)
    suspend fun saveUserToCloud(user: User): Result<Unit>
}