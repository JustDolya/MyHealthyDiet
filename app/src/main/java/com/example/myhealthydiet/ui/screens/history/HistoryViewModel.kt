package com.example.myhealthydiet.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.domain.usecases.history.GetHistoryByPeriodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class DayData(
    val dateLabel: String,      // "Пн", "Вт" и т.д.
    val dateKey: String,        // "2025-03-27" — для группировки
    val timestamp: Long,        // начало дня
    val totalCalories: Int,
    val items: List<ConsumptionHistory>,
)

data class HistoryUiState(
    val days: List<DayData> = emptyList(),
    val selectedDayKey: String? = null,
    val isLoading: Boolean = true,
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryByPeriodUseCase: GetHistoryByPeriodUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

    private val dayKeyFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dayLabelFmt = SimpleDateFormat("EEE", Locale("ru"))
    private val shortDateFmt = SimpleDateFormat("EEE, d MMM", Locale("ru"))

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            getHistoryByPeriodUseCase(GetHistoryByPeriodUseCase.Period.THREE_WEEKS)
                .collect { allItems ->
                    val days = buildDayList(allItems)
                    val todayKey = dayKeyFmt.format(System.currentTimeMillis())
                    _uiState.update {
                        it.copy(
                            days = days,
                            selectedDayKey = it.selectedDayKey ?: todayKey,
                            isLoading = false,
                        )
                    }
                }
        }
    }

    // Строим список всех 21 дня (даже пустых) для графика
    private fun buildDayList(items: List<ConsumptionHistory>): List<DayData> {
        // Группируем записи по дню
        val grouped = items.groupBy { dayKeyFmt.format(it.datetime) }

        val result = mutableListOf<DayData>()
        val cal = Calendar.getInstance()
        // Сдвигаемся на 20 дней назад и идём вперёд
        cal.add(Calendar.DAY_OF_YEAR, -20)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        repeat(21) {
            val key = dayKeyFmt.format(cal.time)
            val label = dayLabelFmt.format(cal.time)
                .replaceFirstChar { c -> c.uppercaseChar() }
            val dayItems = grouped[key] ?: emptyList()
            result.add(
                DayData(
                    dateLabel = label,
                    dateKey = key,
                    timestamp = cal.timeInMillis,
                    totalCalories = dayItems.sumOf { it.calories },
                    items = dayItems,
                )
            )
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return result
    }

    fun selectDay(dateKey: String) {
        _uiState.update { it.copy(selectedDayKey = dateKey) }
    }

    fun selectedDayLabel(dateKey: String): String {
        val today = dayKeyFmt.format(System.currentTimeMillis())
        val yesterday = run {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, -1)
            dayKeyFmt.format(c.time)
        }
        return when (dateKey) {
            today -> "Сегодня"
            yesterday -> "Вчера"
            else -> {
                val parts = dateKey.split("-")
                val cal = Calendar.getInstance()
                cal.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
                shortDateFmt.format(cal.time)
                    .replaceFirstChar { c -> c.uppercaseChar() }
            }
        }
    }
}