package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.local.room.dao.UserDao
import com.example.myhealthydiet.data.mappers.toDomain
import com.example.myhealthydiet.data.mappers.toEntity
import com.example.myhealthydiet.domain.models.User
import com.example.myhealthydiet.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getUser(): Flow<User?> {
        return userDao.getUser().map { it?.toDomain() }
    }

    override suspend fun getUserOnce(): User? {
        return userDao.getUserOnce()?.toDomain()
    }

    override suspend fun saveUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }

    override suspend fun updateSyncTimestamp(timestamp: Long) {
        userDao.updateSyncTimestamp(timestamp)
    }
}