package io.github.ch8n.thoughts

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import io.github.ch8n.thoughts.di.AppDI
import io.github.ch8n.thoughts.ui.navigation.AppNavigator
import io.github.ch8n.thoughts.ui.navigation.Screen
import io.github.ch8n.thoughts.ui.theme.ThoughtsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDI.setAppContext(applicationContext)
        setContent {
            ThoughtsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    AppNavigator(Screen.Splash)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ThoughtsTheme {
        Greeting("Android")
    }
}

@Composable
fun AppKeyboardFocusManager() {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    DisposableEffect(key1 = context) {
        val keyboardManager = KeyBoardManager(context)
        keyboardManager.attachKeyboardDismissListener {
            focusManager.clearFocus()
        }
        onDispose {
            keyboardManager.release()
        }
    }
}

/***
 * Compose issue to be fixed in alpha 1.03
 * track from here : https://issuetracker.google.com/issues/192433071?pli=1
 * current work around
 */
class KeyBoardManager(context: Context) {

    private val activity = context as Activity
    private var keyboardDismissListener: KeyboardDismissListener? = null

    private abstract class KeyboardDismissListener(
        private val rootView: View,
        private val onKeyboardDismiss: () -> Unit
    ) : ViewTreeObserver.OnGlobalLayoutListener {
        private var isKeyboardClosed: Boolean = false
        override fun onGlobalLayout() {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                // 0.15 ratio is right enough to determine keypad height.
                isKeyboardClosed = false
            } else if (!isKeyboardClosed) {
                isKeyboardClosed = true
                onKeyboardDismiss.invoke()
            }
        }
    }

    fun attachKeyboardDismissListener(onKeyboardDismiss: () -> Unit) {
        val rootView = activity.findViewById<View>(android.R.id.content)
        keyboardDismissListener = object : KeyboardDismissListener(rootView, onKeyboardDismiss) {}
        keyboardDismissListener?.let {
            rootView.viewTreeObserver.addOnGlobalLayoutListener(it)
        }
    }

    fun release() {
        val rootView = activity.findViewById<View>(android.R.id.content)
        keyboardDismissListener?.let {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(it)
        }
        keyboardDismissListener = null
    }
}
