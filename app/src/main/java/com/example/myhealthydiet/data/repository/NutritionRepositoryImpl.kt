package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.local.room.dao.DailyNutritionDao
import com.example.myhealthydiet.data.local.room.dao.UserDao
import com.example.myhealthydiet.data.mappers.toDomain
import com.example.myhealthydiet.data.mappers.toEntity
import com.example.myhealthydiet.domain.models.DailyNutrition
import com.example.myhealthydiet.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NutritionRepositoryImpl @Inject constructor(
    private val dailyNutritionDao: DailyNutritionDao,
    private val userDao: UserDao
) : NutritionRepository {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun getDailyNutrition(): Flow<DailyNutrition?> {
        val today = getCurrentDate()
        val userId = 1 // всегда 1 в локальной БД
        return dailyNutritionDao.getDailyNutrition(userId, today).map { it?.toDomain() }
    }

    override suspend fun getDailyNutritionOnce(): DailyNutrition? {
        val today = getCurrentDate()
        val userId = 1
        return dailyNutritionDao.getDailyNutrition(userId, today).first()?.toDomain()
    }

    override suspend fun resetDailyNutrition() {
        val user = userDao.getUserOnce() ?: return
        val today = getCurrentDate()

        val newNutrition = DailyNutrition(
            userId = user.id,
            date = today,
            calories = user.calories,
            proteins = user.proteins,
            fats = user.fats,
            carbs = user.carbs
        )

        dailyNutritionDao.insertDailyNutrition(newNutrition.toEntity())
    }

    override suspend fun subtractNutrition(calories: Int, proteins: Int, fats: Int, carbs: Int) {
        val today = getCurrentDate()
        val userId = 1
        dailyNutritionDao.subtractNutrition(userId, today, calories, proteins, fats, carbs)
    }

    override suspend fun checkAndResetIfNewDay() {
        val user = userDao.getUserOnce() ?: return
        val today = getCurrentDate()
        val userId = user.id

        val nutrition = dailyNutritionDao.getDailyNutrition(userId, today).first()

        if (nutrition == null) {
            // Новый день - создаем запись
            resetDailyNutrition()
        }
    }

    private fun getCurrentDate(): String {
        return dateFormat.format(Date())
    }
}