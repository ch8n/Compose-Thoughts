package io.github.ch8n.thoughts.ui.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.ch8n.thoughts.data.db.Author
import io.github.ch8n.thoughts.data.db.Poem
import io.github.ch8n.thoughts.ui.screen.editor.EditorScreen
import io.github.ch8n.thoughts.ui.screen.home.HomeScreen
import io.github.ch8n.thoughts.ui.screen.splash.Splash
import io.github.ch8n.thoughts.ui.screen.template.feelswithMe.FeelWithMeTemplate
import io.github.ch8n.thoughts.ui.screen.template.feelswithMe.LeTemplateFeelWithMeTemplate
import timber.log.Timber

sealed class Screen {
    object Splash : Screen()
    object Home : Screen()
    data class Editor(val poemId: String, val authorId: String) : Screen()

    sealed class Templates(val label: String) : Screen() {
        data class FeelWithMe(val poemId: String, val authorId: String) : Templates("Feels With Me")
        data class LeThoughtDefault(val poemId: String, val authorId: String) :
            Templates("LeThoughts + Feel With Me")
    }
}

@Composable
fun AppNavigator(splashScreen: Screen) {

    val (backStack, setBackStack) = remember { mutableStateOf<List<Screen>>(listOf(splashScreen)) }
    val context = LocalContext.current

    LaunchedEffect(key1 = backStack) {
        Timber.e("backstack current -> ${backStack.map { it::class.simpleName }.joinToString()}")
    }

    fun onNavigationTo(screen: Screen) {
        setBackStack(backStack + screen)
        Timber.e("backstack navigate ${screen::class.simpleName}")
    }

    fun onNavigationBack() {
        val dropped = backStack.dropLast(1)
        val isSplashScreen = dropped.last() == splashScreen
        if (isSplashScreen) {
            (context as Activity).finish()
        } else {
            setBackStack(dropped)
        }
        Timber.e("backstack onBack ${backStack.map { it::class.simpleName }.joinToString()}")
    }

    BackHandler {
        onNavigationBack()
    }

    when (val top = backStack.last()) {
        is Screen.Splash -> Splash(::onNavigationTo)
        is Screen.Home -> HomeScreen(::onNavigationTo)
        is Screen.Editor -> EditorScreen(
            navigateTo = ::onNavigationTo,
            navigateBack = ::onNavigationBack,
            poemId = top.poemId,
            authorId = top.authorId
        )
        is Screen.Templates.FeelWithMe -> FeelWithMeTemplate(
            poemId = top.poemId,
            authorId = top.authorId
        )
        is Screen.Templates.LeThoughtDefault -> LeTemplateFeelWithMeTemplate(
            poemId = top.poemId,
            authorId = top.authorId
        )
    }

}