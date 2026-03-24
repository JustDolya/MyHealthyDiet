package com.example.myhealthydiet.ui.screens.catalog

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myhealthydiet.ui.navigation.Screen
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black
import com.example.myhealthydiet.ui.theme.SurfaceGray
import com.example.myhealthydiet.ui.theme.TextSecondary

@Composable
fun CatalogScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Каталог",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
        )
        Text(
            text = "Что хотите добавить в рацион?",
            fontSize = 15.sp,
            color = TextSecondary,
        )

        Spacer(Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CatalogTypeCard(
                title = "Блюда",
                subtitle = "Готовые рецепты\nпо категориям",
                icon = Icons.Filled.MenuBook,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.DishCategories.route) },
            )
            CatalogTypeCard(
                title = "Продукты",
                subtitle = "Отдельные\nингредиенты",
                icon = Icons.Filled.ShoppingBasket,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.ProductCategories.route) },
            )
        }
    }
}

@Composable
private fun CatalogTypeCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, BrandOrange, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandOrange,
                modifier = Modifier.size(48.dp),
            )
            Spacer(Modifier.height(12.dp))
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, fontSize = 12.sp, color = TextSecondary)
        }
    }
}