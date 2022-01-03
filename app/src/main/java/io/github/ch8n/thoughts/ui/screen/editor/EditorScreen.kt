package io.github.ch8n.thoughts.ui.screen.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
fun LoadingScaffold(isLoading: Boolean, content: @Composable () -> Unit) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }
    } else {
        content.invoke()
    }
}

@Composable
fun EditorScreen(
    authorId: String,
    poemId: String,
    navigateBack: () -> Unit,
    navigateTo: (Screen.Templates) -> Unit,
) {

    val sharedViewModel = AppDI.sharedViewModel
    val author by sharedViewModel.author.collectAsState()
    val poem by sharedViewModel.displayPoems.collectAsState()
    val selectedPoem = poem.find { it.id == poemId }
    LoadingScaffold(isLoading = selectedPoem == null) {
        val (updatedPoem, setUpdatedPoem) = remember {
            mutableStateOf<Poem>(
                requireNotNull(
                    selectedPoem
                )
            )
        }
        val (info, setInfo) = remember { mutableStateOf(updatedPoem.getContentInfo()) }

        val (isTemplateSelectDialogVisible, setTemplateSelectDialogVisible) = remember {
            mutableStateOf(false)
        }

        val (isDeletePoemDialogVisible, setDeletePoemDialogVisible) = remember {
            mutableStateOf(false)
        }

        BackHandler { navigateBack.invoke() }

        LaunchedEffect(key1 = updatedPoem) {
            sharedViewModel.saveOrUpdatePoem(updatedPoem)
            setInfo.invoke(updatedPoem.getContentInfo())
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Toolbar(
                    navigateBack = navigateBack,
                    setTemplateSelectDialogVisible = setTemplateSelectDialogVisible,
                    setDeleteDialogVisible = setDeletePoemDialogVisible
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    HeaderTextField(
                        updatedPoem = updatedPoem,
                        setUpdatedPoem = setUpdatedPoem
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = info,
                        style = MaterialTheme.typography.caption.copy(
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ContentTextField(
                        updatedPoem = updatedPoem,
                        setUpdatedPoem = setUpdatedPoem
                    )
                }

            }

            if (isTemplateSelectDialogVisible) {
                TemplateSelectionDialog(
                    authorId = authorId,
                    poemId = poemId,
                    navigateBack = {
                        setTemplateSelectDialogVisible.invoke(false)
                    },
                    navigateTo = navigateTo
                )
            }

            if (isDeletePoemDialogVisible) {
                DeleteDialog(
                    onDialogDismiss = {
                        setDeletePoemDialogVisible.invoke(false)
                    },
                    onDeleteConfirmed = {
                        sharedViewModel.deletePoem(requireNotNull(selectedPoem))
                        navigateBack.invoke()
                    }
                )
            }
        }
    }
}

@Composable
private fun ContentTextField(
    updatedPoem: Poem,
    setUpdatedPoem: (Poem) -> Unit
) {
    val (isContentFocused, setContentFocused) = remember { mutableStateOf(false) }
    BasicTextField(
        value = updatedPoem.content,
        onValueChange = {
            setUpdatedPoem.invoke(updatedPoem.copy(content = it))
        },
        textStyle = MaterialTheme.typography.body1.copy(
            color = Color.White,
            lineHeight = 22.sp
        ),
        decorationBox = { innerTextField ->
            Row {
                if (!isContentFocused && updatedPoem.content.isEmpty()) {
                    Text(
                        text = "Write Something Amazing",
                        style = MaterialTheme.typography.body1.copy(
                            color = Silver
                        ),
                    )
                }
                innerTextField()
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .onFocusEvent {
                setContentFocused.invoke(it.isFocused)
            },
        cursorBrush = SolidColor(Color.White)
    )
}

@Composable
private fun HeaderTextField(
    updatedPoem: Poem,
    setUpdatedPoem: (Poem) -> Unit
) {
    val (isHeaderFocused, setHeaderFocused) = remember { mutableStateOf(false) }
    BasicTextField(
        value = updatedPoem.title,
        onValueChange = {
            setUpdatedPoem.invoke(updatedPoem.copy(title = it))
        },
        textStyle = MaterialTheme.typography.h1.copy(
            color = Color.White,
        ),
        decorationBox = { innerTextField ->
            Row {
                if (!isHeaderFocused && updatedPoem.title.isEmpty()) {
                    Text(
                        text = "Input Title...",
                        style = MaterialTheme.typography.h1.copy(
                            color = Silver
                        ),
                    )
                }
                innerTextField()
            }
        },
        modifier = Modifier.onFocusEvent {
            setHeaderFocused.invoke(it.isFocused)
        },
        cursorBrush = SolidColor(Color.White)
    )
}

@Composable
private fun Toolbar(
    navigateBack: () -> Unit,
    setTemplateSelectDialogVisible: (Boolean) -> Unit,
    setDeleteDialogVisible: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ScarletGum,
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
                .clickable { navigateBack() }
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
                    setDeleteDialogVisible.invoke(true)
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
                onClick = { setTemplateSelectDialogVisible.invoke(true) },
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
}

private inline fun Poem.getContentInfo() = "${updatedAt.toDate()} | ${content.length} Words"
