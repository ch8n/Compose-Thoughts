package io.github.ch8n.thoughts.ui.screen.profile

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.utils.checkReadWritePermission
import io.github.ch8n.thoughts.utils.requestReadWritePermissions
import io.github.ch8n.thoughts.utils.writeImageToExternalStorage
import io.github.ch8n.thoughts.utils.writeToExternalStorage


@Composable
fun Profile() {

    requestReadWritePermissions()

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

            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .clip(shape = CircleShape)
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