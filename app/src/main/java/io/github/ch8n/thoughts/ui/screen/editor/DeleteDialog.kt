package io.github.ch8n.thoughts.ui.screen.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.ch8n.thoughts.data.db.Poem
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteDialog(
    onDialogDismiss: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {

    BackHandler {
        onDialogDismiss.invoke()
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDialogDismiss,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background.copy(alpha = 0.7f))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.5.dp,
                        shape = MaterialTheme.shapes.large,
                        brush = Brush.linearGradient(
                            listOf(
                                Color.White,
                                Color.Transparent,
                            ),
                        )
                    )
                    .padding(24.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure you want to Delete ?!",
                    style = MaterialTheme.typography.body2
                )
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(onClick = {
                        onDeleteConfirmed.invoke()
                        onDialogDismiss.invoke()
                    }) {
                        Text(text = "Delete")
                    }

                    OutlinedButton(onClick = onDialogDismiss) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDelete() {
    Preview {
        DeleteDialog(
            onDialogDismiss = {},
            onDeleteConfirmed = {

            }
        )
    }
}