package com.example.myhealthydiet.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myhealthydiet.domain.models.DishCategory
import com.example.myhealthydiet.ui.navigation.Screen
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishCategoriesScreen(
    navController: NavController,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val categories by viewModel.dishCategories.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Категории блюд", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
        }
    ) { innerPadding ->
        if (categories.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = BrandOrange)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(12.dp),
        ) {
            items(categories) { category ->
                DishCategoryCard(
                    category = category,
                    onClick = {
                        navController.navigate(Screen.DishList.createRoute(category.id, category.name))
                    },
                )
            }
        }
    }
}

@Composable
private fun DishCategoryCard(category: DishCategory, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
    ) {
        // Реальное изображение из ресурсов через Coil
        AsyncImage(
            model = category.imagePath,
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        // Затемняющий градиент слева для читаемости текста
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(14.dp)),
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.Black.copy(alpha = 0.45f),
                )
            }
        }
        Text(
            text = category.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
        )
    }
}