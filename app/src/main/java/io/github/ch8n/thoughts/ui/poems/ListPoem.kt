package io.github.ch8n.thoughts.ui.poems

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.data.db.Poem
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.ui.theme.Hibiscus
import io.github.ch8n.thoughts.ui.theme.Koromiko
import io.github.ch8n.thoughts.utils.loremIpsum

@Composable
fun ListPoem(
    poems: List<Poem>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(
            items = poems,
            key = { poem -> poem.id }
        ) { poem ->
            PoemCard(poem = poem)
        }
    }
}


@Composable
private fun PoemCard(poem: Poem) {
    val randomIllustration = remember {
        listOf(
            R.drawable.ic_notebook,
            R.drawable.ic_pencils,
            R.drawable.ic_watch
        ).random()
    }
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 8.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
            ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_quote),
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .alpha(0.7f)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Koromiko)
                            .border(2.5.dp, Hibiscus, CircleShape)
                    )
                }

                if (poem.title.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = poem.title,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (poem.content.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = poem.content,
                        style = MaterialTheme.typography.body1,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = poem.updatedAt.toString(),
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                )
            }
            Image(
                painter = painterResource(id = randomIllustration),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                alpha = 0.25f,
                modifier = Modifier
                    .size(200.dp)
                    .rotate(60f)
                    .offset(x = 60.dp, y = (-25).dp)
                    .align(Alignment.BottomEnd)
            )
        }

    }
}

@Preview
@Composable
fun PoemCardPreview() {
    Preview {
        ListPoem(
            poems = remember {
                listOf(
                    Poem.fake,
                    Poem.fake.copy(title = ""),
                    Poem.fake.copy(content = ""),
                    Poem.fake,
                    Poem.fake,
                    Poem.fake,
                    Poem.fake,
                    Poem.fake,
                )
            }
        )

       // PoemCard(poem = Poem.fake)
    }
}