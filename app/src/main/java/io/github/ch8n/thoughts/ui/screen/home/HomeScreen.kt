package io.github.ch8n.thoughts.ui.screen.home

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.landscapist.glide.GlideImage
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.data.db.Author
import io.github.ch8n.thoughts.data.db.Poem
import io.github.ch8n.thoughts.data.repository.AppRepo
import io.github.ch8n.thoughts.di.AppDI
import io.github.ch8n.thoughts.ui.navigation.Screen
import io.github.ch8n.thoughts.ui.screen.profile.ProfileDialog
import io.github.ch8n.thoughts.ui.theme.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@Composable
private fun TopBar(
    modifier: Modifier,
    onQuery: (query: String) -> Unit,
    onProfileEditClicked: () -> Unit,
    author: Author,
) {
    val (query, setQuery) = remember { mutableStateOf("") }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            imageModel = if (author.avatarUri.isEmpty()) author.placeholder else Uri.parse(author.avatarUri),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .offset(y = 4.dp, x = (-4).dp)
                .size(48.dp)
                .clip(CircleShape)
                .border(2.5.dp, Koromiko, CircleShape)
                .clickable {
                    onProfileEditClicked()
                }
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
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                )
            },
            maxLines = 1
        )
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenRoot(
    poems: List<Poem>,
    onSearch: (query: String) -> Unit,
    onPoemClicked: (poem: Poem) -> Unit,
    onProfileEditClicked: () -> Unit,
    onCreateNewPoem: () -> Unit,
    author: Author,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(bottom = 16.dp, top = 88.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Divider(
                        thickness = 6.dp,
                        color = Color.White,
                        modifier = Modifier
                            .width(200.dp)
                            .padding(16.dp)
                            .align(Alignment.Center)
                            .clip(MaterialTheme.shapes.large),
                    )
                }
            }

            items(
                items = poems,
                key = { poem -> poem.id }
            ) { poem ->
                PoemCard(
                    poem = poem,
                    onPoemClicked = onPoemClicked
                )
            }
        }

        TopBar(
            author = author,
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Violet,
                            ScarletGum,
                            Color.Transparent
                        )
                    )
                )
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 48.dp),
            onQuery = onSearch,
            onProfileEditClicked = onProfileEditClicked
        )

        FloatingActionButton(
            onClick = {
                onCreateNewPoem.invoke()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            backgroundColor = Purple
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_create),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(36.dp)
                    .rotate(-70f)
            )
        }

    }

}

@Composable
private fun PoemCard(
    poem: Poem,
    onPoemClicked: (poem: Poem) -> Unit
) {
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
            .fillMaxWidth()
            .border(0.5.dp, Color.White, MaterialTheme.shapes.large)
            .clickable {
                onPoemClicked.invoke(poem)
            },
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


class SharedViewModel(
    private val appRepo: AppRepo
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val _displayPoems = MutableStateFlow(emptyList<Poem>())
    private val _author = MutableStateFlow(Author.Default)
    private val _allPoems = MutableStateFlow(emptyList<Poem>())
    val displayPoems = _displayPoems.asStateFlow()
    val author = _author.asStateFlow()

    fun filterPoem(word: String) {
        query.value = word
    }

    init {
        viewModelScope.launch {
            appRepo
                .getAuthors()
                .map { it.first() }
                .catch { error -> Log.e("author", "error", error) }
                .collect {
                    _author.emit(it)
                }

            _author
                .flatMapMerge {
                    appRepo.getAllPoems(it)
                }
                .catch { error ->
                    Log.e("getAllPoems", "error", error)
                    _allPoems.emit(emptyList())
                }
                .collect {
                    _allPoems.emit(it)
                }

            query.debounce(300)
                .flatMapMerge { _query ->
                    if (_query.isEmpty()) {
                        _allPoems
                    } else {
                        _allPoems.map {
                            it.filter { poem ->
                                poem.content.contains(_query, ignoreCase = true)
                                        || poem.title.contains(_query, ignoreCase = true)
                            }
                        }
                    }
                }
                .catch { error ->
                    Log.e("filterPoem", "error", error)
                    _displayPoems.emit(_allPoems.value)
                }
                .collect {
                    _displayPoems.emit(it)
                }
        }

    }


    fun updateAuthor(author: Author) {
        viewModelScope.launch {
            appRepo.addAuthor(author)
        }
    }

    fun saveOrUpdatePoem(updatedPoem: Poem) {
        viewModelScope.launch {
            appRepo.addPoem(updatedPoem)
        }
    }

}

@Composable
fun HomeScreen(
    navigateTo: (Screen) -> Unit
) {
    val viewModel = remember { AppDI.sharedViewModel }
    val author by viewModel.author.collectAsState(initial = Author.Default)
    val poems by viewModel.displayPoems.collectAsState(initial = emptyList())
    val context = LocalContext.current

    val (isProfileVisible, setProfileVisible) = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        HomeScreenRoot(
            author = author,
            poems = poems,
            onSearch = { query ->
                viewModel.filterPoem(query)
            },
            onPoemClicked = {
                navigateTo(
                    Screen.Editor(
                        poem = it,
                        author = author
                    )
                )
            },
            onProfileEditClicked = { setProfileVisible.invoke(true) },
            onCreateNewPoem = {
                navigateTo(
                    Screen.Editor(
                        poem = Poem(title = "", content = "", authorId = author.uid),
                        author = author
                    )
                )
            }
        )
        if (isProfileVisible) {
            ProfileDialog(
                author = author,
                activity = context as Activity,
                navigateBack = {
                    setProfileVisible.invoke(false)
                },
                onDefaultAuthorUpdated = {
                    viewModel.updateAuthor(it)
                }
            )
        }
    }
}