package io.github.ch8n.thoughts.ui.screen.profile

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.dhaval2404.imagepicker.ImagePicker
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.data.db.Author
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.ui.theme.Hibiscus
import io.github.ch8n.thoughts.ui.theme.Koromiko
import timber.log.Timber


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileDialog(
    activity: Activity,
    navigateBack: () -> Unit,
    author: Author,
    onDefaultAuthorUpdated: (updatedAuthor: Author) -> Unit
) {

    BackHandler {
        navigateBack.invoke()
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            navigateBack.invoke()
        },
    ) {
        val (avatarUri, setAvatarUri) = remember { mutableStateOf<Uri?>(null) }

        val (isAvatarError, setAvatarError) = remember { mutableStateOf<Boolean>(false) }

        val launcherProfileImageResult =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (data?.data != null) {
                            val uri = requireNotNull(data.data)
                            setAvatarUri.invoke(uri)
                        }
                        setAvatarError.invoke(data?.data == null)
                    }
                    ImagePicker.RESULT_ERROR -> {
                        val error = ImagePicker.getError(data)
                        setAvatarError.invoke(true)
                        Timber.e("Avatar image picker error $error")
                    }
                    else -> {
                        val msg = "Task Cancelled"
                        setAvatarError.invoke(true)
                    }
                }
            }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background.copy(alpha = 0.7f))
        ) {

            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                val (updatedName, setUpdatedName) = remember { mutableStateOf(author.name) }

                GlideImage(
                    imageModel = avatarUri ?: if (author.avatarUri.isNullOrEmpty()) {
                        author.placeholder
                    } else {
                        author.avatarUri
                    },
                    contentScale = ContentScale.Crop,
                    circularReveal = CircularReveal(duration = 250),
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, if (isAvatarError) Hibiscus else Koromiko, CircleShape)
                        .clickable {
                            setAvatarError.invoke(false)
                            ImagePicker
                                .with(activity)
                                .galleryOnly()
                                .crop()
                                .compress(1024)
                                .maxResultSize(620, 620)
                                .createIntent { intent ->
                                    launcherProfileImageResult.launch(intent)
                                }
                        },
                    placeHolder = painterResource(id = R.drawable.ic_avatar),
                )

                Spacer(modifier = Modifier.width(24.dp))


                Column {

                    OutlinedTextField(
                        value = updatedName,
                        onValueChange = setUpdatedName,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Author Name?") },
                        shape = MaterialTheme.shapes.medium,
                        maxLines = 1
                    )

                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(onClick = {
                            onDefaultAuthorUpdated.invoke(
                                author.copy(
                                    name = if (updatedName.isEmpty()) author.name else updatedName,
                                    avatarUri = avatarUri?.toString() ?: author.avatarUri
                                )
                            )
                            navigateBack.invoke()
                        }) {
                            Text(text = "Save")
                        }

                        OutlinedButton(onClick = navigateBack) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
}
