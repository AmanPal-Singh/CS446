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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.goosebuddy.ui.screens.Utility.formatTime
import com.example.goosebuddy.ui.theme.Red
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun RoutineTimer(
    navController: NavController,
    name: String,
    duration: Duration,
    routine: Routine
) {

    val viewModel by remember {
        mutableStateOf(RoutineTimerViewModel(duration))
    }
    val time by viewModel.time.observeAsState(duration.inWholeMilliseconds.formatTime())
    val progress by viewModel.progress.observeAsState(1.00F)
    val isPlaying by viewModel.isPlaying.observeAsState(false)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = name)
        Text(text = time)
        Box {
            CircularProgressIndicatorBackground(
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp),
                color = Red,
                stroke = 15
            )
            CircularProgressIndicator(
                progress = progress,
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
            Button(onClick = { /** Navigate to last subroutine of routine */ }) {
                Text("Back")
            }
            Button(onClick = {
                viewModel.handleCountdownTimer()
            }) {
                Text(if (isPlaying) "Pause" else "Play")
            }
            Button(onClick = { /** Navigate to next subroutine of routine */  }) {
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