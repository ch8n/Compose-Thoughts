package io.github.ch8n.thoughts.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Koromiko,
    primaryVariant = ScarletGum,
    onPrimary = Color.White,
    secondary = Hibiscus,
    onSecondary = Color.Black,
    background = Violet,
    onBackground = Color.White,
    surface = ScarletGum,
    onSurface = Color.White,
)

@Composable
fun ThoughtsTheme(content: @Composable() () -> Unit) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Violet,
            darkIcons = false
        )
    }
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}