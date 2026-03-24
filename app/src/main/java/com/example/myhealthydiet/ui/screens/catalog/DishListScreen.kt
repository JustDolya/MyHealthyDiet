package com.example.myhealthydiet.ui.screens.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myhealthydiet.domain.models.Dish
import com.example.myhealthydiet.ui.navigation.Screen
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.White

private val dishEmojis = listOf("🍳","🥣","🍲","🥘","🍝","🥗","🍱","🥩","🍗","🥚","🫕","🍜","🥙","🧆","🍛","🥧","🍮","🧁")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishListScreen(
    navController: NavController,
    categoryId: Int,
    categoryName: String,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    // Фикс #2: явно выставляем категорию при входе на экран
    LaunchedEffect(categoryId) {
        viewModel.setDishCategory(categoryId)
    }

    val dishes by viewModel.dishesByCategory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
        if (dishes.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = BrandOrange)
            }
            return@Scaffold
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(dishes) { dish ->
                DishCard(
                    dish = dish,
                    emoji = dishEmojis.getOrElse(dish.id % dishEmojis.size) { "🍽" },
                    onClick = {
                        navController.navigate(Screen.DishDetail.createRoute(dish.id))
                    },
                )
            }
        }
    }
}

@Composable
private fun DishCard(dish: Dish, emoji: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BrandOrange.copy(alpha = 0.5f), BrandOrange.copy(alpha = 0.9f))
                    )
                )
        )
        Text(
            text = emoji,
            fontSize = 52.sp,
            modifier = Modifier.align(Alignment.Center).padding(bottom = 24.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.45f)),
        ) {
            Text(
                text = dish.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
            )
        }
    }
}