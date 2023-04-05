package com.example.goosebuddy.ui.shared.components

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.R

enum class GooseState { Pressed, Idle }

enum class GooseVariation {
    Default,
    Waving
}

fun getGooseResource(variation: GooseVariation): Int {
    if (variation == GooseVariation.Default) {
        return R.drawable.buddy
    } else if (variation == GooseVariation.Waving) {
        return  R.drawable.waving_goose
    }
    return R.drawable.buddy
}

@Composable
fun Goose(
    variation: GooseVariation = GooseVariation.Default,
    size: Dp,
    rotationZ: Float = 0f,
    honkSound: Boolean = false,
    modifier: Modifier = Modifier
) {

    var state by remember { mutableStateOf(GooseState.Idle) }
    val ty by animateFloatAsState(if (state == GooseState.Pressed) 0f else 20f)

    val mContext = LocalContext.current
    val mMediaPlayer = MediaPlayer.create(mContext, R.raw.honk_sound)
    val imageModifier = modifier
        .size(size = size)
        .graphicsLayer(
            rotationZ = rotationZ,
            translationY = ty,
        )
        .clickable(
            onClick = {
                if (honkSound) mMediaPlayer.start()

            },
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        )
        .pointerInput(state) {
            awaitPointerEventScope {
                state = if (state == GooseState.Pressed) {
                    waitForUpOrCancellation()
                    GooseState.Idle
                } else {
                    awaitFirstDown(false)
                    GooseState.Pressed
                }
            }
        }
    Image(
        painter = painterResource(id = getGooseResource(variation)),
        contentDescription = "Goose Image",
        contentScale = ContentScale.Fit,
        modifier = imageModifier
    )
}
