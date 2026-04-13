package com.example.myhealthydiet.ui.screens.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.myhealthydiet.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    initialCategoryId: Int,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val categories by viewModel.productCategories.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Поля формы
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }

    // Категория — начинаем с той, из которой открыли экран
    var selectedCategoryId by remember { mutableIntStateOf(initialCategoryId) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val selectedCategoryName = categories.find { it.id == selectedCategoryId }?.name ?: "Выберите категорию"

    LaunchedEffect(uiState.addSuccess) {
        if (uiState.addSuccess) {
            viewModel.clearAddSuccess()
            navController.popBackStack()
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
                title = { Text("Новый продукт", fontSize = 20.sp) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // Название
            BrandTextField(
                value = name,
                onValueChange = { name = it },
                label = "Название продукта",
            )
            Spacer(Modifier.height(12.dp))

            // Выбор категории
            Text("Категория", fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Box {
                OutlinedButton(
                    onClick = { dropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = selectedCategoryName,
                        modifier = Modifier.weight(1f),
                        color = Black,
                    )
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = BrandOrange)
                }
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f),
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategoryId = category.id
                                dropdownExpanded = false
                            },
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("КБЖУ на 100 г:", fontSize = 14.sp, color = TextSecondary)
            Spacer(Modifier.height(8.dp))

            // КБЖУ в ряд
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BrandTextField(
                    value = calories,
                    onValueChange = { calories = it.filter { c -> c.isDigit() } },
                    label = "Ккал",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                BrandTextField(
                    value = proteins,
                    onValueChange = { proteins = it.filter { c -> c.isDigit() } },
                    label = "Белки, г",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BrandTextField(
                    value = fats,
                    onValueChange = { fats = it.filter { c -> c.isDigit() } },
                    label = "Жиры, г",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                BrandTextField(
                    value = carbs,
                    onValueChange = { carbs = it.filter { c -> c.isDigit() } },
                    label = "Углеводы, г",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }

            Spacer(Modifier.height(32.dp))

            if (uiState.isLoading) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(color = BrandOrange)
                }
            } else {
                BrandButton(
                    text = "Сохранить продукт",
                    onClick = {
                        viewModel.addCustomProduct(
                            name = name,
                            categoryId = selectedCategoryId,
                            calories = calories.toIntOrNull() ?: 0,
                            proteins = proteins.toIntOrNull() ?: 0,
                            fats = fats.toIntOrNull() ?: 0,
                            carbs = carbs.toIntOrNull() ?: 0,
                        )
                    },
                )
            }
        }
    }
}