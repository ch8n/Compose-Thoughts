package io.github.ch8n.thoughts.ui.components.scaffolds

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.ch8n.thoughts.ui.theme.ThoughtsTheme

@Composable
fun Preview(content: @Composable () -> Unit) {
    ThoughtsTheme {
        Surface(color = MaterialTheme.colors.surface) {
            content.invoke()
        }
    }
}