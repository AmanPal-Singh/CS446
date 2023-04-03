package com.example.goosebuddy.ui.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.R

@Composable
fun Goose(size: Dp, rotationZ: Float = 0f) {
    val imageModifier = Modifier
        .size(size = size)
        .graphicsLayer(
            rotationZ = rotationZ,
        )
    Image(
        painter = painterResource(id = R.drawable.buddy),
        contentDescription = "Goose Image",
        contentScale = ContentScale.Fit,
        modifier = imageModifier
    )
}
