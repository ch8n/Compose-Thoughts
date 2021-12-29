package io.github.ch8n.thoughts.ui.screen.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.utils.loremIpsum

@Composable
fun EditorScreen() {
    val (header, setHeader) = remember { mutableStateOf(loremIpsum(8)) }
    val (content, setContent) = remember { mutableStateOf(loremIpsum(200)) }
    val (info, setInfo) = remember { mutableStateOf("${System.currentTimeMillis()}| ${content.length} Words") }

    LaunchedEffect(key1 = content) {
        setInfo.invoke(
            "${System.currentTimeMillis()}| ${content.length} Words"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        0.7f to MaterialTheme.colors.primaryVariant,
                        0.7f to MaterialTheme.colors.primaryVariant,
                        0.9f to Color.Transparent
                    )
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .clickable {
                        //TODO on backpress
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
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .clickable {
                        //TODO on share
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
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
                                color = Color.Gray
                            ),
                        )
                    } else {
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            BasicTextField(
                value = info,
                onValueChange = {},
                textStyle = MaterialTheme.typography.caption.copy(
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
                                color = Color.Gray
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
}


@Preview
@Composable
fun EditorScreenPreview() {
    Preview {
        EditorScreen()
    }
}
