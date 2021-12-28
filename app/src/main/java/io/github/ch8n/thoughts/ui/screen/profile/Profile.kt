package io.github.ch8n.thoughts.ui.screen.profile

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.dhaval2404.imagepicker.ImagePicker
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.utils.requestReadWritePermissions


@Composable
fun Profile() {

    val context = LocalContext.current
    val (avatarBitmap, setAvatarBitmap) = remember { mutableStateOf<Uri?>(null) }
    requestReadWritePermissions()
    val launcherProfileImageResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, fileUri)
                    setAvatarBitmap.invoke(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    val error = ImagePicker.getError(data)
                }
                else -> {
                    val msg = "Task Cancelled"
                }
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            val (author, setAuthor) = remember { mutableStateOf("") }

            GlideImage(
                imageModel = avatarBitmap,
                contentScale = ContentScale.Crop,
                circularReveal = CircularReveal(duration = 250),
                placeHolder = ImageBitmap.imageResource(R.drawable.ic_photo_camera_black_48dp),
                error = ImageBitmap.imageResource(R.drawable.ic_photo_black_48dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(0.6f)
                    .clip(shape = CircleShape)
                    .background(Color.White)
                    .clickable {
                        ImagePicker
                            .with(context as Activity)
                            .galleryOnly()
                            .crop()
                            .compress(1024)
                            .maxResultSize(620, 620)
                            .createIntent { intent ->
                                launcherProfileImageResult.launch(intent)
                            }
                    },
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = author,
                onValueChange = setAuthor,
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                ),
                label = {
                    Text(text = "Author Name")
                }
            )

        }
    }
}


@Preview
@Composable
fun ProfilePreview() {
    Preview {
        Profile()
    }
}