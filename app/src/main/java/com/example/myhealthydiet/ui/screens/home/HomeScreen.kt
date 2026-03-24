package com.example.myhealthydiet.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myhealthydiet.domain.models.ConsumptionHistory
import com.example.myhealthydiet.domain.models.DailyNutrition
import com.example.myhealthydiet.domain.models.User
import com.example.myhealthydiet.ui.navigation.Screen
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.SurfaceGray
import com.example.myhealthydiet.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Цвета секторов
private val ColorProteins = Color(0xFF4CAF50)
private val ColorFats     = Color(0xFFFF5722)
private val ColorCarbs    = Color(0xFF2196F3)
private val ColorEmpty    = Color(0xFFE0E0E0)

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Catalog.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Добавить продукт") },
                containerColor = BrandOrange,
                contentColor = Black,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BrandOrange)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            // Заголовок
            item {
                Text(
                    text = "Рацион на сегодня",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                )
            }

            // Диаграмма + остатки КБЖУ
            item {
                NutritionCard(
                    user = uiState.user,
                    nutrition = uiState.dailyNutrition,
                )
            }

            // Заголовок списка
            item {
                Text(
                    text = "Потреблённые продукты:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black,
                )
            }

            // Список продуктов
            if (uiState.todayHistory.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .border(1.dp, ColorEmpty, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Ничего не добавлено", color = TextSecondary)
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BrandOrange, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp)),
                    ) {
                        uiState.todayHistory.forEachIndexed { index, item ->
                            HistoryItem(item)
                            if (index < uiState.todayHistory.lastIndex) {
                                Divider(color = ColorEmpty)
                            }
                        }
                    }
                }
            }

            // Отступ под FAB
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

// ─── Карточка с диаграммой и КБЖУ ────────────────────────────────────────────

@Composable
private fun NutritionCard(user: User?, nutrition: DailyNutrition?) {
    val targetCalories = user?.calories ?: 0
    val targetProteins = user?.proteins ?: 0
    val targetFats     = user?.fats     ?: 0
    val targetCarbs    = user?.carbs    ?: 0

    // DailyNutrition хранит остаток, потреблено = цель − остаток
    val consumedCalories = (targetCalories - (nutrition?.calories ?: targetCalories)).coerceAtLeast(0)
    val consumedProteins = (targetProteins - (nutrition?.proteins ?: targetProteins)).coerceAtLeast(0)
    val consumedFats     = (targetFats     - (nutrition?.fats     ?: targetFats    )).coerceAtLeast(0)
    val consumedCarbs    = (targetCarbs    - (nutrition?.carbs    ?: targetCarbs   )).coerceAtLeast(0)

    val remainCalories = (nutrition?.calories ?: targetCalories).coerceAtLeast(0)
    val remainProteins = (nutrition?.proteins ?: targetProteins).coerceAtLeast(0)
    val remainFats     = (nutrition?.fats     ?: targetFats    ).coerceAtLeast(0)
    val remainCarbs    = (nutrition?.carbs    ?: targetCarbs   ).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BrandOrange, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Диаграмма
        Box(contentAlignment = Alignment.Center) {
            NutritionPieChart(
                proteins = consumedProteins.toFloat(),
                fats     = consumedFats.toFloat(),
                carbs    = consumedCarbs.toFloat(),
                modifier = Modifier.size(220.dp),
            )
            // Текст в центре
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$consumedCalories",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                )
                Text(
                    text = "/ $targetCalories ккал",
                    fontSize = 13.sp,
                    color = TextSecondary,
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Легенда + остатки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            NutrientInfo("Б", remainProteins, targetProteins, ColorProteins)
            NutrientInfo("Ж", remainFats,     targetFats,     ColorFats)
            NutrientInfo("У", remainCarbs,    targetCarbs,    ColorCarbs)
        }
    }
}

@Composable
private fun NutrientInfo(label: String, remain: Int, target: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Spacer(Modifier.width(4.dp))
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Black)
        }
        Text(
            text = "осталось $remain г",
            fontSize = 12.sp,
            color = TextSecondary,
        )
        Text(
            text = "из $target г",
            fontSize = 11.sp,
            color = TextSecondary,
        )
    }
}

// ─── Canvas PieChart (donut) ──────────────────────────────────────────────────

@Composable
private fun NutritionPieChart(
    proteins: Float,
    fats: Float,
    carbs: Float,
    modifier: Modifier = Modifier,
) {
    val total = proteins + fats + carbs

    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.18f
        val radius = (size.minDimension - strokeWidth) / 2f
        val topLeft = Offset(center.x - radius, center.y - radius)
        val arcSize = Size(radius * 2, radius * 2)
        val style = Stroke(width = strokeWidth)

        if (total <= 0f) {
            // Пустая диаграмма
            drawArc(
                color = ColorEmpty,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = style,
            )
            return@Canvas
        }

        val proteinsAngle = (proteins / total) * 360f
        val fatsAngle     = (fats     / total) * 360f
        val carbsAngle    = (carbs    / total) * 360f

        var startAngle = -90f

        listOf(
            proteinsAngle to ColorProteins,
            fatsAngle     to ColorFats,
            carbsAngle    to ColorCarbs,
        ).forEach { (sweep, color) ->
            if (sweep > 0f) {
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = style,
                )
                startAngle += sweep
            }
        }
    }
}

// ─── Элемент списка истории ───────────────────────────────────────────────────

@Composable
private fun HistoryItem(item: ConsumptionHistory) {
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(item.datetime))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Тип: продукт или блюдо
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (item.isDish) BrandOrange.copy(alpha = 0.15f) else SurfaceGray,
                    CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (item.isDish) "🍽" else "🥗",
                fontSize = 16.sp,
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Black)
            Text(
                text = if (item.isDish) "$time · ${item.grams}% порции"
                else "$time · ${item.grams} г",
                fontSize = 12.sp,
                color = TextSecondary,
            )
        }
        Text(
            text = "${item.calories} ккал",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandOrange,
        )
    }
}