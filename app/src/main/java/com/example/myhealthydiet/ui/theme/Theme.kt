package com.example.myhealthydiet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BrandOrange,
    onPrimary = Black,
    primaryContainer = BrandOrangeContainer,
    onPrimaryContainer = Black,
    secondary = BrandOrangeDark,
    onSecondary = White,
    secondaryContainer = BrandOrangeLight,
    onSecondaryContainer = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = TextSecondary,
    outline = OutlineColor,
    outlineVariant = BrandOrange,
)

@Composable
fun MyHealthyDietTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}