package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.ui.theme.Grey
import com.example.goosebuddy.ui.theme.Red
import com.example.goosebuddy.ui.theme.Yellow
import com.example.goosebuddy.ui.theme.Green


class Routine(
    // Represents a routine
    var title: String,
    var completedSteps: Int,
    var totalSteps: Int,
)

// TODO: used to stub routines, will update for demo 2
val mockRoutines = arrayOf(
    Routine("Skincare", 10, 10),
    Routine("Fitness", 75, 100),
    Routine("Yoga", 0, 10),
    Routine("Cleaning", 5, 10),
    Routine("Study", 25, 100),
)

fun getColour(progress: Float): Color {
    if (progress == 1.0f) {
        return Green
    } else if (progress > 0.25f) {
        return Yellow
    } else {
        return Red
    }
}
@Composable
fun Routines() {
    Surface() {
        Column {
            mockRoutines.forEach { item ->
                RoutineBlock(item = item)
            }
        }
    }
}

@Composable
fun RoutineBlock(item: Routine) {
    Surface() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.totalSteps == item.completedSteps,
                onCheckedChange = {
                    item.completedSteps = item.totalSteps
                },
                colors=CheckboxDefaults.colors(
                        checkedColor = Green,
                        uncheckedColor = Grey,
            )
            )
            Column {
                Text(item.title)
                LinearProgressIndicator(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .clip(CircleShape)
                        .height(8.dp),
                    progress = item.completedSteps.toFloat()/item.totalSteps,
                    color = getColour(item.completedSteps.toFloat()/item.totalSteps),
                    backgroundColor = Grey,
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "More",
                    tint = Grey
                )
            }
        }
    }
}

@Preview
@Composable
fun RoutineBlockPreview() {
    RoutineBlock(mockRoutines[2])
}