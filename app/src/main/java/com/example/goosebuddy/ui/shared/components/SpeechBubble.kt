package com.example.goosebuddy.ui.shared.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.ui.theme.LightBlue

@Composable
@Preview
fun SpeechBubble(
    text: String = "Honk honk!",
    includeLeftSpacing: Boolean = true,
) {
    val shape = RoundedCornerShape(50)
    Row {
        if (includeLeftSpacing) {
            Spacer(modifier = Modifier
                .width(125.dp)
                .height(intrinsicSize = IntrinsicSize.Max)
            )
        }
        Column() {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .border(shape = shape, width = 2.dp, color = Color.Transparent)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .background(LightBlue)
                        .padding(10.dp),
                ) {
                    Text(text)
                }
            }
            SpeechBubbleTriangleArrow()
        }
        Spacer(modifier = Modifier.size(30.dp))
    }

}

@Composable
fun SpeechBubbleTriangleArrow(){
    Canvas(
        modifier = Modifier
            .height(20.dp),
        onDraw = {
            val size = 20.dp.toPx()
            val offsetFromLeft = 10.dp.toPx()
            val trianglePath = Path().apply {
                // Moves to top center position
                moveTo(0f + offsetFromLeft, 0f)
                // Add line to bottom right corner
                lineTo(size + offsetFromLeft, 0f)
                // Add line to bottom left corner
                lineTo(size / 2f + offsetFromLeft, size)
            }
            drawPath(
                color = LightBlue,
                path = trianglePath
            )
        }
    )

}
