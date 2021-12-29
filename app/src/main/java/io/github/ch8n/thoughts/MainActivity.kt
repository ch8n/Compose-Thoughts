package io.github.ch8n.thoughts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.github.ch8n.thoughts.di.AppDI
import io.github.ch8n.thoughts.ui.screen.poems.PoemCardPreview
import io.github.ch8n.thoughts.ui.theme.ThoughtsTheme
import io.github.ch8n.thoughts.ui.theme.Violet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDI.setAppContext(applicationContext)
        setContent {
            ThoughtsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PoemCardPreview()
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