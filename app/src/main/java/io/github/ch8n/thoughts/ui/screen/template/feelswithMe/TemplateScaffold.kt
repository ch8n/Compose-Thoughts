package io.github.ch8n.thoughts.ui.screen.template.feelswithMe

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.ui.theme.Koromiko
import io.github.ch8n.thoughts.utils.CaptureBitmap
import io.github.ch8n.thoughts.utils.writeImageToExternalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.MathContext
import java.util.*

@Composable
fun TemplateScaffold(content: @Composable () -> Unit) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val (bitmap, setBitmap) = remember { mutableStateOf<Bitmap?>(null) }
    val (requestKey, setRequestKey) = remember { mutableStateOf(UUID.randomUUID().toString()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CaptureBitmap(
            captureRequestKey = requestKey,
            content = content,
            onBitmapCaptured = {
                setBitmap.invoke(it)
            })

        if (bitmap == null) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.BottomCenter),
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "",
                tint = Koromiko,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .padding(8.dp)
                    .clickable {
                        scope.launch {
                            saveAndShare(bitmap, context)
                        }
                    }
                    .align(Alignment.BottomCenter),
            )
        }
    }
}


suspend fun saveAndShare(bitmap: Bitmap?, context: Context) {
    val image = bitmap ?: return Toast.makeText(
        context,
        "failed to create shareable image",
        Toast.LENGTH_SHORT
    ).show()

    val uri = saveImage(image, context) ?: return Toast.makeText(
        context,
        "failed to create shareable image",
        Toast.LENGTH_SHORT
    ).show()

    shareImageUri(context, uri)
}

suspend fun saveImage(image: Bitmap, context: Context): Uri? =
    withContext(Dispatchers.IO) {
        val imagesFolder = File(context.cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(context, "io.github.ch8n.thoughts", file)
        } catch (e: IOException) {
            Log.d("Error", "IOException while trying to write file for sharing: " + e.message)
        }
        uri
    }


fun shareImageUri(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.type = "image/png"
    context.startActivity(intent)
}