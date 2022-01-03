package io.github.ch8n.thoughts.ui.screen.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.data.db.Author
import io.github.ch8n.thoughts.data.db.Poem
import io.github.ch8n.thoughts.di.AppDI
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.ui.navigation.Screen
import io.github.ch8n.thoughts.ui.screen.home.SharedViewModel
import io.github.ch8n.thoughts.ui.screen.template.TemplateSelectionDialog
import io.github.ch8n.thoughts.ui.theme.Silver
import io.github.ch8n.thoughts.ui.theme.ScarletGum
import io.github.ch8n.thoughts.utils.toDate


@Composable
fun EditorScreen(
    author: Author,
    poem: Poem,
    navigateBack: () -> Unit,
    navigateTo: (Screen.Templates) -> Unit,
) {
    val sharedViewModel = AppDI.sharedViewModel
    val (header, setHeader) = remember { mutableStateOf(poem.title) }
    val (content, setContent) = remember { mutableStateOf(poem.content) }
    val (info, setInfo) = remember { mutableStateOf("${System.currentTimeMillis().toDate()} | ${content.length} Words") }
    val (isTemplateVisible, setTemplateVisible) = remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler {
        navigateBack.invoke()
    }

    LaunchedEffect(key1 = content, header) {
        savePoem(header, content, sharedViewModel, poem)
        setInfo.invoke(
            "${System.currentTimeMillis().toDate()} | ${content.length} Words"
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ScarletGum,
                                Color.Transparent
                            )
                        )
                    )
                    .height(72.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                ) {

                    IconButton(
                        onClick = {
                            sharedViewModel.deletePoem(poem)
                            navigateBack.invoke()
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { setTemplateVisible.invoke(true) },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                BasicTextField(
                    value = header,
                    onValueChange = setHeader,
                    textStyle = MaterialTheme.typography.h1.copy(
                        color = Color.White,
                    ),
                    decorationBox = { innerTextField ->
                        if (header.isEmpty()) {
                            Text(
                                text = "Input Title...",
                                style = MaterialTheme.typography.h1.copy(
                                    color = Silver
                                ),
                            )
                        } else {
                            innerTextField()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = info,
                    style = MaterialTheme.typography.caption.copy(
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = content,
                    onValueChange = setContent,
                    textStyle = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        lineHeight = 22.sp
                    ),
                    decorationBox = { innerTextField ->
                        if (content.isEmpty()) {
                            Text(
                                text = "Write Something Amazing",
                                style = MaterialTheme.typography.body1.copy(
                                    color = Silver
                                ),
                            )
                        } else {
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }

        }

        if (isTemplateVisible) {
            TemplateSelectionDialog(
                author = author,
                poem = poem,
                navigateBack = {
                    setTemplateVisible.invoke(false)
                },
                navigateTo = navigateTo
            )
        }
    }

}

private fun savePoem(
    header: String,
    content: String,
    sharedViewModel: SharedViewModel,
    poem: Poem
) {
    val isContentUpdated = header != poem.title || content != poem.content
    if (isContentUpdated){
        sharedViewModel.saveOrUpdatePoem(
            poem.copy(
                title = header,
                content = content,
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}

@Preview
@Composable
fun EditorScreenPreview() {
    Preview {
        EditorScreen(
            author = Author.fake,
            poem = Poem.fake,
            navigateBack = {

            },
            navigateTo = {

            }
        )
    }
}
