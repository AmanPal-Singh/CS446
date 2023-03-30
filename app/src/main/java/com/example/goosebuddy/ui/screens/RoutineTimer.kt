package com.example.goosebuddy.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import android.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.ui.theme.Red
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun RoutineTimer(name: String, duration: Duration) {
    var ticks by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while(ticks <= duration.inWholeMilliseconds) {
            delay(1.milliseconds)
            ticks++
            println(ticks/duration.inWholeMilliseconds)
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = ticks/duration.inWholeMilliseconds,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = name)
        Box {
            CircularProgressIndicatorBackground(
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp),
                color = Red,
                stroke = 15
            )
            CircularProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp),
                strokeWidth = 15.dp
            )
        }

        // TODO: Replace these buttons with icons later
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text("Back")
            }
            Button(onClick = { /*TODO*/ }) {
                Text("Play")
            }
            Button(onClick = { /*TODO*/ }) {
                Text("Next")
            }
        }
    }
}

@Composable
fun CircularProgressIndicatorBackground(
    modifier: Modifier,
    color: Color,
    stroke: Int
) {
    val style = with(LocalDensity.current) { Stroke(stroke.dp.toPx()) }
    Canvas(
        modifier = modifier,
        onDraw = {
            val innerRadius = (size.minDimension - style.width) / 2
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 360f,
                topLeft = Offset(
                    (size / 2.0f).width - innerRadius,
                    (size / 2.0f).height - innerRadius
                ),
                size = Size(innerRadius * 2, innerRadius * 2),
                useCenter = false,
                style = style
            )
        }
    )
}