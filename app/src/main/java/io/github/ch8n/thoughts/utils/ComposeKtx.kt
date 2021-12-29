package io.github.ch8n.thoughts.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.IOException
import java.io.OutputStream

fun loremIpsum(words: Int = 500): String {
    return LoremIpsum(words).values.joinToString()
}

val isMinSdk29 get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

inline fun runSdk29Plus(onFalse: () -> Unit = {}, onTrue: () -> Unit) {
    if (isMinSdk29) {
        onTrue.invoke()
    } else {
        onFalse.invoke()
    }
}

fun Context.checkReadWritePermission(): Pair<Boolean, Boolean> {
    kotlin.runCatching { }

    val readPermission = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    val writePermission = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    return Pair(readPermission, writePermission || minSdk29)
}

@Composable
fun requestReadWritePermissions() {
    val context = LocalContext.current
    var isReadPermissionGranted = remember { false }
    var isWritePermissionGranted = remember { false }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMap ->
        isReadPermissionGranted =
            permissionMap.get(Manifest.permission.READ_EXTERNAL_STORAGE) ?: isReadPermissionGranted
        isWritePermissionGranted = permissionMap.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ?: isWritePermissionGranted
    }

    LaunchedEffect(context) {
        val permissionStatus = context.checkReadWritePermission()
        isReadPermissionGranted = permissionStatus.first
        isWritePermissionGranted = permissionStatus.second
        val requestPermission = mutableListOf<String>()
        if (!isReadPermissionGranted) {
            requestPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isWritePermissionGranted) {
            requestPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (requestPermission.isNotEmpty()) {
            permissionLauncher.launch(requestPermission.toTypedArray())
        }
    }
}

fun Context.imageDecoderFromUriCompat(fileUri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(
            contentResolver,
            fileUri
        )
    } else {

        val source = ImageDecoder.createSource(contentResolver, fileUri)
        ImageDecoder.decodeBitmap(source)
    }
}


fun imagesExternalStorageUriCompat(): Uri {
    return if (isMinSdk29) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
}

fun Context.writeToExternalStorage(
    location: Uri,
    contentValues: ContentValues,
    onOutputStreamReady: (outputStream: OutputStream) -> Unit
) {
    val metaFileUri = contentResolver.insert(location, contentValues)
        ?: throw IOException("Can't create meta file")
    contentResolver.openOutputStream(metaFileUri).use {
        val outputStream = it ?: throw IOException("failed to open output stream")
        onOutputStreamReady.invoke(outputStream)
    }
}

fun Context.writeImageToExternalStorage(bitmap: Bitmap): Boolean {
    return runCatching {
        writeToExternalStorage(
            location = imagesExternalStorageUriCompat(),
            contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "avatar.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bitmap.width)
                put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            },
            onOutputStreamReady = { outputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                    throw IOException("failed to write bitmap")
                }
            }
        )
    }.isSuccess
}