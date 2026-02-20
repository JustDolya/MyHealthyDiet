package com.example.myhealthydiet.domain.usecases.profile

import com.example.myhealthydiet.domain.models.User
import com.example.myhealthydiet.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User?> {
        return userRepository.getUser()
    }
}