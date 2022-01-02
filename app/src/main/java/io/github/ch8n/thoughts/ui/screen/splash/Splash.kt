package io.github.ch8n.thoughts.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import io.github.ch8n.thoughts.R
import io.github.ch8n.thoughts.ui.components.scaffolds.Preview
import io.github.ch8n.thoughts.ui.theme.Caveat
import androidx.compose.ui.tooling.preview.Preview
import io.github.ch8n.thoughts.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun Splash(navigateTo: (Screen) -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.notebook_animation))
    val progress by animateLottieCompositionAsState(composition)

    LaunchedEffect(key1 = progress) {
        if (progress == 1f) {
            delay(1000)
            navigateTo.invoke(Screen.Home)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.3f),
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = progress == 1f) {
                Text(
                    text = "Le`Thoughts",
                    style = TextStyle.Default.copy(
                        fontFamily = Caveat,
                        color = Color.White,
                        fontSize = 32.sp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashPreview() {
    Preview {
        Splash(
            navigateTo = {

            }
        )
    }
}