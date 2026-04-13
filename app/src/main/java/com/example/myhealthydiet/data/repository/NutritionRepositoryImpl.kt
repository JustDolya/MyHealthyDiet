package com.example.myhealthydiet.data.repository

import com.example.myhealthydiet.data.local.room.dao.ConsumptionHistoryDao
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
    private val userDao: UserDao,
    private val historyDao: ConsumptionHistoryDao,
) : NutritionRepository {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun getDailyNutrition(): Flow<DailyNutrition?> {
        val today = getCurrentDate()
        return dailyNutritionDao.getDailyNutrition(1, today).map { it?.toDomain() }
    }

    override suspend fun getDailyNutritionOnce(): DailyNutrition? {
        return dailyNutritionDao.getDailyNutrition(1, getCurrentDate()).first()?.toDomain()
    }

    override suspend fun resetDailyNutrition() {
        val user = userDao.getUserOnce() ?: return
        val today = getCurrentDate()
        val nutrition = DailyNutrition(
            userId = user.id,
            date = today,
            calories = user.calories,
            proteins = user.proteins,
            fats = user.fats,
            carbs = user.carbs,
        )
        dailyNutritionDao.insertDailyNutrition(nutrition.toEntity())
    }

    override suspend fun subtractNutrition(calories: Int, proteins: Int, fats: Int, carbs: Int) {
        dailyNutritionDao.subtractNutrition(1, getCurrentDate(), calories, proteins, fats, carbs)
    }

    override suspend fun checkAndResetIfNewDay() {
        val user = userDao.getUserOnce() ?: return
        val today = getCurrentDate()
        val existing = dailyNutritionDao.getDailyNutrition(1, today).first()

        if (existing == null) {
            // Новый день — считаем сколько уже съедено СЕГОДНЯ (на случай перезахода в тот же день)
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val todayStart = cal.timeInMillis
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val todayEnd = cal.timeInMillis

            val totals = historyDao.getDailyTotals(1, todayStart, todayEnd)

            // Остаток = норма − уже съеденное
            val remainCalories = (user.calories - (totals?.totalCalories ?: 0)).coerceAtLeast(0)
            val remainProteins = (user.proteins - (totals?.totalProteins ?: 0)).coerceAtLeast(0)
            val remainFats     = (user.fats     - (totals?.totalFats     ?: 0)).coerceAtLeast(0)
            val remainCarbs    = (user.carbs    - (totals?.totalCarbs    ?: 0)).coerceAtLeast(0)

            val nutrition = DailyNutrition(
                userId = 1,
                date = today,
                calories = remainCalories,
                proteins = remainProteins,
                fats = remainFats,
                carbs = remainCarbs,
            )
            dailyNutritionDao.insertDailyNutrition(nutrition.toEntity())
        }
    }

    private fun getCurrentDate(): String = dateFormat.format(Date())
}