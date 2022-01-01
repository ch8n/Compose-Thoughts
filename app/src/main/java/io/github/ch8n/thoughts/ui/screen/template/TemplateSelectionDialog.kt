package io.github.ch8n.thoughts.ui.screen.template


import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.data.db.Author
import io.github.ch8n.thoughts.data.db.Poem
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.ui.navigation.Screen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TemplateSelectionDialog(
    author: Author,
    poem: Poem,
    navigateBack: () -> Unit,
    navigateTo: (Screen.Templates) -> Unit
) {

    val templates = remember {
        listOf(
            Screen.Templates.LeThoughtDefault(author, poem),
            Screen.Templates.FeelWithMe(author, poem),
        )
    }

    BackHandler {
        navigateBack.invoke()
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = navigateBack,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background.copy(alpha = 0.4f))
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .clip(MaterialTheme.shapes.large)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colors.surface),
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(24.dp)
                        .clickable {
                            navigateBack()
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Back",
                        style = MaterialTheme.typography.button.copy(
                            color = Color.White
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Select Template",
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    templates.forEach {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it.label,
                            style = MaterialTheme.typography.body2.copy(
                                color = Color.White
                            ),
                            modifier = Modifier.clickable {
                                navigateTo.invoke(it)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            thickness = 1.5.dp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun TemplateSelectionDialogPreview() {
    Preview {
        TemplateSelectionDialog(
            author = Author.fake,
            poem = Poem.fake,
            navigateBack = {

            },
            navigateTo = {

            }
        )
    }
}