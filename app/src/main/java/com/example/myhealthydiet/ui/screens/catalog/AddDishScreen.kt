package com.example.myhealthydiet.ui.screens.catalog

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myhealthydiet.ui.components.BrandButton
import com.example.myhealthydiet.ui.components.BrandTextField
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.SurfaceGray
import com.example.myhealthydiet.ui.theme.TextSecondary
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDishScreen(
    navController: NavController,
    initialCategoryId: Int,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val categories by viewModel.dishCategories.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Поля формы
    var name by remember { mutableStateOf("") }
    var minutesToCook by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }  // одна строка = один ингредиент
    var steps by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    // Категория
    var selectedCategoryId by remember { mutableIntStateOf(initialCategoryId) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val selectedCategoryName = categories.find { it.id == selectedCategoryId }?.name
        ?: "Выберите категорию"

    // Лаунчер галереи
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Копируем картинку во внутреннее хранилище чтобы работало офлайн
            val savedPath = copyImageToAppStorage(context, it)
            imageUri = savedPath
        }
    }

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
                title = { Text("Новое блюдо", fontSize = 20.sp) },
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

            // ── Фото блюда ────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BrandOrange, RoundedCornerShape(12.dp))
                    .background(SurfaceGray)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center,
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Фото блюда",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.AddPhotoAlternate,
                            contentDescription = null,
                            tint = BrandOrange,
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Нажмите, чтобы выбрать фото", fontSize = 13.sp, color = TextSecondary)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Название ──────────────────────────────────────────────────────
            BrandTextField(value = name, onValueChange = { name = it }, label = "Название блюда")
            Spacer(Modifier.height(12.dp))

            // ── Категория ─────────────────────────────────────────────────────
            Text("Категория", fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Box {
                OutlinedButton(
                    onClick = { dropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(selectedCategoryName, modifier = Modifier.weight(1f), color = Black)
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

            Spacer(Modifier.height(12.dp))

            // ── Время приготовления ───────────────────────────────────────────
            BrandTextField(
                value = minutesToCook,
                onValueChange = { minutesToCook = it.filter { c -> c.isDigit() } },
                label = "Время приготовления, мин.",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(Modifier.height(12.dp))

            // ── Ингредиенты ───────────────────────────────────────────────────
            Text("Каждый ингредиент с новой строки: \"Творог - 200 г\"", fontSize = 12.sp, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            BrandTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = "Ингредиенты",
                singleLine = false,
            )
            Spacer(Modifier.height(12.dp))

            // ── Шаги ──────────────────────────────────────────────────────────
            Text("Каждый шаг с новой строки", fontSize = 12.sp, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            BrandTextField(
                value = steps,
                onValueChange = { steps = it },
                label = "Шаги приготовления",
                singleLine = false,
            )
            Spacer(Modifier.height(16.dp))

            // ── КБЖУ ─────────────────────────────────────────────────────────
            Text("КБЖУ (средние значения на 100 грамм):", fontSize = 14.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
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
                    text = "Сохранить блюдо",
                    onClick = {
                        // Парсим ингредиенты из текста
                        val parsedIngredients = ingredients
                            .lines()
                            .filter { it.isNotBlank() }
                            .map { line ->
                                val parts = line.split("-", limit = 2)
                                com.example.myhealthydiet.domain.models.IngredientItem(
                                    name = parts.getOrNull(0)?.trim() ?: line.trim(),
                                    amount = parts.getOrNull(1)?.trim() ?: "",
                                )
                            }
                        viewModel.addCustomDish(
                            name = name,
                            categoryId = selectedCategoryId,
                            minutesToCook = minutesToCook.toIntOrNull() ?: 0,
                            ingredients = parsedIngredients,
                            steps = steps,
                            calories = calories.toIntOrNull() ?: 0,
                            proteins = proteins.toIntOrNull() ?: 0,
                            fats = fats.toIntOrNull() ?: 0,
                            carbs = carbs.toIntOrNull() ?: 0,
                            imageUri = imageUri,
                        )
                    },
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// Копирует выбранное изображение во внутреннее хранилище приложения
// и возвращает путь к копии. Это нужно, чтобы картинка была доступна
// после того, как пользователь закроет галерею или перезапустит приложение.
private fun copyImageToAppStorage(context: Context, uri: Uri): String {
    val dir = File(context.filesDir, "dish_images").also { it.mkdirs() }
    val file = File(dir, "${UUID.randomUUID()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(file).use { output -> input.copyTo(output) }
    }
    return file.absolutePath
}