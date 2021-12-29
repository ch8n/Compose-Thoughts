package io.github.ch8n.thoughts.ui.screen.poems

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
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
fun SearchPoem(
    modifier: Modifier,
    onQuery: (query: String) -> Unit
) {
    val (query, setQuery) = remember { mutableStateOf("") }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .offset(y=4.dp,x= (-4).dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(Koromiko)
                .border(2.5.dp, Hibiscus, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {
                setQuery.invoke(it)
                onQuery.invoke(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Search")
            },
            shape = MaterialTheme.shapes.medium,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription ="",
                    modifier = Modifier.size(20.dp),
                )
            },
            maxLines = 1
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListPoem(
    poems: List<Poem>,
    onSearch: (query: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        stickyHeader {
            SearchPoem(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            0.7f to MaterialTheme.colors.surface,
                            0.7f to MaterialTheme.colors.surface,
                            0.9f to Color.Transparent
                        )
                    )
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 32.dp),
                onQuery = onSearch
            )
        }
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
                Icon(
                    painter = painterResource(id = R.drawable.ic_quote),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .alpha(0.7f)
                )

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

        val list = remember {
            listOf(
                Poem.fake,
                Poem.fake.copy(title = ""),
                Poem.fake.copy(content = ""),
                Poem.fake.copy(content = "${loremIpsum(20)} pokemon"),
                Poem.fake,
                Poem.fake,
                Poem.fake,
                Poem.fake,
            )
        }

        val (displayList, setDisplayList) = remember { mutableStateOf(list) }



        ListPoem(
            poems = displayList,
            onSearch = { query ->
                setDisplayList(
                    if (query.isEmpty()) {
                        list
                    } else {
                        list.filter {
                            it.title.contains(query, ignoreCase = true)
                                    || it.content.contains(query, ignoreCase = true)
                        }
                    }
                )
            }
        )
        // PoemCard(poem = Poem.fake)
    }
}