package io.github.ch8n.thoughts.ui.screen.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.utils.loremIpsum

@Composable
fun Editor(
    content: String,
    author: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(56.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(shape = CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = author,
                    fontSize = 24.sp,
                    style = TextStyle(color = Color.White)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = content,
                fontSize = 32.sp,
                style = TextStyle(color = Color.White),
                fontStyle = FontStyle.Italic,
            )
        }

    }
}

@Preview
@Composable
fun EditorPreview() {
    Preview {
        val (content, setContent) = remember { mutableStateOf("Rejection never pains, but being a forced option pierce my heart in two...!") }
        val (author, setAuthor) = remember { mutableStateOf("Pooja Srivastava") }

        Editor(
            content = content,
            author = author
        )
    }
}