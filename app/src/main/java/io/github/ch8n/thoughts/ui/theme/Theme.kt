package io.github.ch8n.thoughts.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Hibiscus,
    primaryVariant = ScarletGum,
    onPrimary = Color.White,
    secondary = Koromiko,
    onSecondary = Color.Black,
    background = Violet,
    onBackground = Color.White,
    surface = ScarletGum,
    onSurface = Color.White,
)

@Composable
fun ThoughtsTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}