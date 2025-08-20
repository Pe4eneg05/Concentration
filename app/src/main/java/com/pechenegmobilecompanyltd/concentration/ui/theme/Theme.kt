package com.pechenegmobilecompanyltd.concentration.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4ECDC4),
    secondary = Color(0xFFFF6B6B),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White.copy(alpha = 0.8f)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4ECDC4),
    secondary = Color(0xFFFF6B6B),
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black.copy(alpha = 0.8f)
)

@Composable
fun FocusConcentrationTheme(
    darkTheme: Boolean = true, // По умолчанию темная тема
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}