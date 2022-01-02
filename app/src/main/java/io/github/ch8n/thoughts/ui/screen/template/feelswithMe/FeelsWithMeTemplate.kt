package io.github.ch8n.thoughts.ui.screen.template.feelswithMe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import io.github.ch8n.thoughts.data.db.Author
import io.github.ch8n.thoughts.data.db.Poem

@Composable
fun FeelWithMeContent(
    poem: Poem,
    author: Author,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .background(Black),
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
                GlideImage(
                    imageModel = author.avatarUri,
                    contentScale = ContentScale.Crop,
                    circularReveal = CircularReveal(duration = 250),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, White, CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = author.name,
                    fontSize = 28.sp,
                    style = TextStyle(color = White)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = poem.content,
                fontSize = 20.sp,
                style = TextStyle(color = White),
                fontStyle = Italic,
            )
        }

    }
}

@Composable
fun FeelWithMeTemplate(poem: Poem, author: Author) {
    TemplateScaffold {
        FeelWithMeContent(
            poem = poem,
            author = author,
            modifier = Modifier.fillMaxSize()
        )
    }
}


