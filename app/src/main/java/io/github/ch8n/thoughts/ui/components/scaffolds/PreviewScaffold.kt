package io.github.ch8n.thoughts.ui.components.scaffolds

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.ch8n.thoughts.ui.theme.ThoughtsTheme

@Composable
fun Preview(isDarkTheme: Boolean = true, content: @Composable () -> Unit) {
    ThoughtsTheme(darkTheme = isDarkTheme) {
        content.invoke()
    }
}