package com.example.goosebuddy.ui.shared.components

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
    Waving,
    Holding,
}

enum class GooseAccessory {
    None,
    Flag,
    Heart,
    Pencil,
    Book,
    Clipboard
}

interface Goose {
    @Composable
    fun decorate()
}

class DefaultGoose: Goose {
    @Composable
    override fun decorate() = gooseRender()
}

class WavingGoose: Goose {
    @Composable
    override fun decorate() = gooseRender(GooseVariation.Waving)
}

class HoldingGoose: Goose {
    @Composable
    override fun decorate() = gooseRender(GooseVariation.Holding)
}

abstract class GooseDecorator (private val goose: Goose) : Goose {
    @Composable
    override fun decorate() {
        return goose.decorate()
    }
}

class FlagAccessory(goose: Goose) : GooseDecorator(goose) {
    @Composable
    override fun decorate() {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-50).dp)
        ) {
            super.decorate()
            decorateWithFlagAccessory()
        }

    }

    @Composable
    private fun decorateWithFlagAccessory() {
        accessoryRender(accessory = GooseAccessory.Flag)
    }
}

class HeartAccessory(goose: Goose) : GooseDecorator(goose) {
    @Composable
    override fun decorate() {
        Row {
            super.decorate()
            decorateWithFlagAccessory()
        }

    }

    @Composable
    private fun decorateWithFlagAccessory() {
        accessoryRender(accessory = GooseAccessory.Heart)
    }
}


class PencilAccessory(goose: Goose) : GooseDecorator(goose) {
    @Composable
    override fun decorate(): Unit {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-70).dp)
        ) {
            super.decorate()
            decorateWithFlagAccessory()
        }

    }

    @Composable
    private fun decorateWithFlagAccessory() {
        accessoryRender(accessory = GooseAccessory.Pencil)
    }
}

class BookAccessory(goose: Goose) : GooseDecorator(goose) {
    @Composable
    override fun decorate(): Unit {
        Row {
            super.decorate()
            decorateWithFlagAccessory()
        }

    }

    @Composable
    private fun decorateWithFlagAccessory() {
        accessoryRender(accessory = GooseAccessory.Book)
    }
}

class ClipboardAccessory(goose: Goose) : GooseDecorator(goose) {
    @Composable
    override fun decorate(): Unit {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-70).dp)
        ) {
            super.decorate()
            decorateWithFlagAccessory()
        }
    }

    @Composable
    private fun decorateWithFlagAccessory() {
        accessoryRender(
            accessory = GooseAccessory.Clipboard,
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer(
                    rotationZ = 20f,
                )
        )
    }
}



fun getGooseResource(variation: GooseVariation): Int {
    return when (variation) {
        GooseVariation.Default -> R.drawable.buddy
        GooseVariation.Waving -> R.drawable.waving_goose
        GooseVariation.Holding -> R.drawable.holding_goose
    }
}

fun getAccessoryResource(accessory: GooseAccessory): Int? {
    if (accessory == GooseAccessory.Flag) {
        return R.drawable.flag
    } else if (accessory == GooseAccessory.Heart) {
        return R.drawable.heart
    } else if (accessory == GooseAccessory.Pencil) {
        return R.drawable.pencil_accessory
    } else if (accessory == GooseAccessory.Book) {
        return R.drawable.book
    } else if (accessory == GooseAccessory.Clipboard) {
        return R.drawable.clipboard
    }
    return null
}

@Composable
fun gooseRender(
    pose: GooseVariation = GooseVariation.Default,
    size: Dp = 200.dp,
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
            painter = painterResource(id = getGooseResource(pose)),
            contentDescription = "Goose Image",
            contentScale = ContentScale.Fit,
            modifier = imageModifier
        )

}

@Composable
fun accessoryRender(
    accessory: GooseAccessory,
    modifier: Modifier = Modifier
) {
    val accessoryId = getAccessoryResource(accessory)
    if (accessoryId != null) {
        Image(
            painter = painterResource(id = accessoryId),
            contentDescription = "Goose Image",
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
    }
}