package kv.compose.videoplayervk.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.shimmer() = composed {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    graphicsLayer(
        translationX = translateAnim
    )
} 