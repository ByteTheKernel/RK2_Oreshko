package com.example.rk_2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable

// Настройка цветовых схем для светлой и тёмной тем
private val LightColors = lightColors(
    primary = Blue500,
    primaryVariant = Blue700,
    secondary = Teal200
)

private val DarkColors = darkColors(
    primary = Blue200,
    primaryVariant = Blue700,
    secondary = Teal200
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
