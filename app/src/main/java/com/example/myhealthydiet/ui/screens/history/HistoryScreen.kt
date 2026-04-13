package com.example.myhealthydiet.ui.screens.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.SurfaceGray
import com.example.myhealthydiet.ui.theme.TextSecondary
import com.example.myhealthydiet.ui.theme.White

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = BrandOrange)
        }
        return
    }

    val selectedDay = uiState.days.find { it.dateKey == uiState.selectedDayKey }
    val maxCalories = uiState.days.maxOfOrNull { it.totalCalories }?.coerceAtLeast(1) ?: 1

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Spacer(Modifier.height(16.dp))
            // ── Заголовок ─────────────────────────────────────────────────────
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
            ) {
                Text(
                    text = "${selectedDay?.totalCalories ?: 0} ккал",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                )
                Text(
                    text = uiState.selectedDayKey?.let { viewModel.selectedDayLabel(it) } ?: "",
                    fontSize = 15.sp,
                    color = TextSecondary,
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── График ────────────────────────────────────────────────────────
            BarChart(
                days = uiState.days,
                selectedKey = uiState.selectedDayKey,
                maxCalories = maxCalories,
                onDayClick = { viewModel.selectDay(it) },
            )

            Spacer(Modifier.height(20.dp))
        }

        // ── Список съеденного ─────────────────────────────────────────────────
        if (selectedDay == null || selectedDay.items.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceGray),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("За этот день ничего не записано", color = TextSecondary)
                }
            }
        } else {
            item {
                Text(
                    text = uiState.selectedDayKey?.let { viewModel.selectedDayLabel(it) } ?: "",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(8.dp))
            }
            itemsIndexed(selectedDay.items) { index, item ->
                HistoryListItem(item)
                if (index < selectedDay.items.lastIndex) {
                    Divider(
                        color = Color(0xFFEEEEEE),
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

// ── Бар-чарт ─────────────────────────────────────────────────────────────────

@Composable
private fun BarChart(
    days: List<DayData>,
    selectedKey: String?,
    maxCalories: Int,
    onDayClick: (String) -> Unit,
) {
    val listState = rememberLazyListState()

    // Прокручиваем к сегодняшнему дню при старте
    LaunchedEffect(days.isNotEmpty()) {
        if (days.isNotEmpty()) {
            listState.scrollToItem((days.size - 7).coerceAtLeast(0))
        }
    }

    val barColor = BrandOrange
    val selectedBarColor = BrandOrange
    val inactiveBarColor = BrandOrange.copy(alpha = 0.3f)
    val gridColor = Color(0xFFDDDDDD)

    Column {
        // Горизонтальные линии-ориентиры (6 делений)
        val chartHeight = 160.dp

        Box(modifier = Modifier.fillMaxWidth().height(chartHeight + 28.dp)) {
            // Сетка
            Canvas(modifier = Modifier.fillMaxWidth().height(chartHeight).padding(horizontal = 8.dp)) {
                val lines = 3
                repeat(lines + 1) { i ->
                    val y = size.height * (1f - i.toFloat() / lines)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx(),
                    )
                }
            }

            // Подписи оси Y
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .height(chartHeight),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                listOf(maxCalories, maxCalories * 2 / 3, maxCalories / 3, 0).forEach { v ->
                    Text(
                        text = if (v >= 1000) "${v / 1000}k" else "$v",
                        fontSize = 10.sp,
                        color = TextSecondary,
                    )
                }
            }

            // Бары
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 28.dp)
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp),
            ) {
                items(days) { day ->
                    val isSelected = day.dateKey == selectedKey
                    val fraction = day.totalCalories.toFloat() / maxCalories
                    val color = when {
                        isSelected -> selectedBarColor
                        day.totalCalories == 0 -> Color.Transparent
                        else -> inactiveBarColor
                    }

                    Column(
                        modifier = Modifier
                            .width(36.dp)
                            .clickable { onDayClick(day.dateKey) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(chartHeight),
                            contentAlignment = Alignment.BottomCenter,
                        ) {
                            if (fraction > 0f) {
                                Box(
                                    modifier = Modifier
                                        .width(28.dp)
                                        .height(chartHeight * fraction)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(color),
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = day.dateLabel,
                            fontSize = 11.sp,
                            color = if (isSelected) BrandOrange else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

// ── Элемент списка ────────────────────────────────────────────────────────────

@Composable
private fun HistoryListItem(item: ConsumptionHistory) {
    val time = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        .format(java.util.Date(item.datetime))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Иконка-аватар
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (item.isDish) BrandOrange else BrandOrange.copy(alpha = 0.4f)),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Black)
            Text(
                text = if (item.isDish) "$time · ${item.grams}% порции"
                else "$time · ${item.grams} г",
                fontSize = 12.sp,
                color = TextSecondary,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${item.calories} ккал",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandOrange,
            )
            Text(
                text = "Б${item.proteins} Ж${item.fats} У${item.carbs}",
                fontSize = 11.sp,
                color = TextSecondary,
            )
        }
    }
}