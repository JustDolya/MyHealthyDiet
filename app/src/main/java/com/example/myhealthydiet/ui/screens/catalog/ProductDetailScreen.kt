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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myhealthydiet.ui.components.BrandButton
import com.example.myhealthydiet.ui.components.BrandTextField
import com.example.myhealthydiet.ui.navigation.Screen
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.SurfaceGray
import com.example.myhealthydiet.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(productId) { viewModel.loadProductById(productId) }
    val product = uiState.selectedProduct
    val snackbarHostState = remember { SnackbarHostState() }
    var gramsInput by remember { mutableStateOf("100") }

    // После успешного добавления — переходим на Home
    LaunchedEffect(uiState.addSuccess) {
        if (uiState.addSuccess) {
            viewModel.clearAddSuccess()
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = false }
                launchSingleTop = true
            }
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
                title = { Text("Продукт", fontWeight = FontWeight.Bold) },
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
        if (product == null) {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = BrandOrange)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(product.name, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Black)
            if (product.isCustom) {
                Text("Пользовательский продукт", fontSize = 13.sp, color = BrandOrange)
            }

            Spacer(Modifier.height(20.dp))

            Text("КБЖУ на 100 г:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Black)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                NutrientChip("Ккал", product.calories.toString(), Modifier.weight(1f))
                NutrientChip("Б", "${product.proteins} г", Modifier.weight(1f))
                NutrientChip("Ж", "${product.fats} г", Modifier.weight(1f))
                NutrientChip("У", "${product.carbs} г", Modifier.weight(1f))
            }

            Spacer(Modifier.height(28.dp))

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
                    Spacer(Modifier.height(12.dp))
                    BrandTextField(
                        value = gramsInput,
                        onValueChange = { gramsInput = it.filter { c -> c.isDigit() } },
                        label = "Количество, г",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Spacer(Modifier.height(8.dp))

                    val grams = gramsInput.toIntOrNull()?.coerceIn(1, 9999) ?: 100
                    val multiplier = grams / 100.0
                    Text(
                        text = "≈ ${(product.calories * multiplier).toInt()} ккал  |  " +
                                "Б: ${(product.proteins * multiplier).toInt()} г  " +
                                "Ж: ${(product.fats * multiplier).toInt()} г  " +
                                "У: ${(product.carbs * multiplier).toInt()} г",
                        fontSize = 13.sp,
                        color = BrandOrange,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(Modifier.height(12.dp))

                    if (uiState.isLoading) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            CircularProgressIndicator(
                                color = BrandOrange,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    } else {
                        BrandButton(
                            text = "Добавить в рацион",
                            onClick = {
                                val g = gramsInput.toIntOrNull() ?: 100
                                viewModel.addProductToRation(product.id, g)
                            },
                        )
                    }
                }
            }
        }
    }
}