package com.example.myhealthydiet.ui.screens.catalog

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.ui.components.BrandButton
import com.example.myhealthydiet.ui.components.BrandTextField
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.SurfaceGray
import com.example.myhealthydiet.ui.theme.TextSecondary
import com.example.myhealthydiet.ui.theme.White

private val dishDetailEmojis = listOf("🍳","🥣","🍲","🥘","🍝","🥗","🍱","🥩","🍗","🥚","🫕","🍜","🥙","🧆","🍛","🥧","🍮","🧁")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailScreen(
    navController: NavController,
    dishId: Int,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    // Фикс #3: загружаем по id напрямую
    LaunchedEffect(dishId) { viewModel.loadDishById(dishId) }
    val dish = uiState.selectedDish
    val snackbarHostState = remember { SnackbarHostState() }
    var portionInput by remember { mutableStateOf("100") }

    LaunchedEffect(uiState.addSuccess) {
        if (uiState.addSuccess) {
            snackbarHostState.showSnackbar("Добавлено в рацион!")
            viewModel.clearAddSuccess()
        }
    }
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рецепт", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandOrange,
                    titleContentColor = Black,
                    navigationIconContentColor = Black,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        if (dish == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BrandOrange)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            // ── Шапка с эмодзи вместо фото ───────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                BrandOrange.copy(alpha = 0.4f),
                                BrandOrange.copy(alpha = 0.85f),
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = dishDetailEmojis.getOrElse(dish.id % dishDetailEmojis.size) { "🍽" },
                    fontSize = 80.sp,
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {

                // ── Название ──────────────────────────────────────────────────
                Text(dish.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Black)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Время приготовления: ${dish.minutesToCook / 60} ч. ${dish.minutesToCook % 60} мин.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                )

                Spacer(Modifier.height(16.dp))

                // ── КБЖУ ─────────────────────────────────────────────────────
                Text("КБЖУ (на 100 грамм порции):", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Black)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    NutrientChip("Ккал", dish.calories.toString(), Modifier.weight(1f))
                    NutrientChip("Б", "${dish.proteins} г", Modifier.weight(1f))
                    NutrientChip("Ж", "${dish.fats} г", Modifier.weight(1f))
                    NutrientChip("У", "${dish.carbs} г", Modifier.weight(1f))
                }

                Spacer(Modifier.height(16.dp))

                // ── Ингредиенты ───────────────────────────────────────────────
                Text("Ингредиенты:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Black)
                Spacer(Modifier.height(8.dp))
                dish.ingredients.forEach { ingredient ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text("• ", color = BrandOrange, fontWeight = FontWeight.Bold)
                        Text("${ingredient.name} — ${ingredient.amount}", fontSize = 14.sp, color = Black)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Шаги приготовления ────────────────────────────────────────
                Text("Шаги приготовления:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Black)
                Spacer(Modifier.height(8.dp))
                dish.steps.split("\n").forEachIndexed { index, step ->
                    if (step.isNotBlank()) {
                        Text(
                            text = "Шаг ${index + 1}: $step",
                            fontSize = 14.sp,
                            color = Black,
                            modifier = Modifier.padding(vertical = 3.dp),
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ── Добавить в рацион ─────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BrandOrange, RoundedCornerShape(12.dp))
                        .background(SurfaceGray, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                ) {
                    Column {
                        Text(
                            "Добавить в рацион",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Black,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Укажите размер порции (100 = целое блюдо)",
                            fontSize = 13.sp,
                            color = TextSecondary,
                        )
                        Spacer(Modifier.height(12.dp))
                        BrandTextField(
                            value = portionInput,
                            onValueChange = { portionInput = it.filter { c -> c.isDigit() } },
                            label = "Размер порции, г",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        Spacer(Modifier.height(8.dp))

                        // Предпросмотр КБЖУ для выбранной порции
                        val portion = portionInput.toIntOrNull()?.coerceIn(1, 999) ?: 100
                        val multiplier = portion / 100.0
                        Text(
                            text = "≈ ${(dish.calories * multiplier).toInt()} ккал  |  " +
                                    "Б: ${(dish.proteins * multiplier).toInt()} г  " +
                                    "Ж: ${(dish.fats * multiplier).toInt()} г  " +
                                    "У: ${(dish.carbs * multiplier).toInt()} г",
                            fontSize = 13.sp,
                            color = BrandOrange,
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(Modifier.height(12.dp))

                        if (uiState.isLoading) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                CircularProgressIndicator(color = BrandOrange, modifier = Modifier.size(40.dp))
                            }
                        } else {
                            BrandButton(
                                text = "Добавить в рацион",
                                onClick = {
                                    val p = portionInput.toIntOrNull() ?: 100
                                    viewModel.addDishToRation(dish.id, p)
                                },
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
public fun NutrientChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(BrandOrange.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Black)
        Text(label, fontSize = 11.sp, color = TextSecondary)
    }
}