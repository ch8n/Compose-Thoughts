package io.github.ch8n.thoughts.ui.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.ch8n.thoughts.data.db.Poem
import io.github.ch8n.thoughts.ui.screen.editor.EditorScreen
import io.github.ch8n.thoughts.ui.screen.poems.PoemCardPreview

sealed class Screen {
    object Home : Screen()
    data class Editor(val poem: Poem) : Screen()
    object Preview : Screen()
    object Profile : Screen()
}

@Composable
fun AppNavigator(startScreen: Screen) {

    val (backStack, setBackStack) = remember { mutableStateOf<List<Screen>>(listOf(startScreen)) }
    val context = LocalContext.current

    BackHandler {
        setBackStack(backStack.dropLast(1))
        Log.e(
            "backstack",
            "pressed back"
        )
    }

    LaunchedEffect(key1 = backStack) {
        Log.e(
            "backstack",
            "current -> ${backStack.map { it::class.simpleName }.joinToString(",")}"
        )
    }

    fun navigate(screen: Screen) {
        setBackStack(backStack + screen)
        Log.e(
            "backstack",
            "navigate"
        )
    }

    fun back() {
        setBackStack(backStack.dropLast(1))
        Log.e(
            "backstack",
            "back"
        )
    }

    when (val top = backStack.lastOrNull()) {
        is Screen.Editor -> EditorScreen(poem = top.poem, ::back)
        is Screen.Home -> PoemCardPreview(::navigate)
        is Screen.Preview -> TODO()
        is Screen.Profile -> TODO()
        null -> (context as Activity).finish()
    }

}