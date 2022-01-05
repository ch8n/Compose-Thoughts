package io.github.ch8n.thoughts.ui.components.scaffolds

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.tooling.preview.Preview
import io.github.ch8n.thoughts.ui.theme.ThoughtsTheme

@Composable
fun Preview(content: @Composable () -> Unit) {
    ThoughtsTheme {
        Surface(color = MaterialTheme.colors.surface, modifier = Modifier.fillMaxSize()) {
            content.invoke()
        }
    }
}