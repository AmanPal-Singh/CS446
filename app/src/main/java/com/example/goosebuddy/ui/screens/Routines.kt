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


sealed class RoutineItem(var title: String, var progress: Int) {

    fun completeRoutine() {
        this.progress = 100
    }
    object Skincare: RoutineItem("Skincare", 100)
    object Fitness: RoutineItem("Fitness", 75)
    object Yoga: RoutineItem("Yoga", 0)
    object Cleaning: RoutineItem("Cleaning", 50)
    object Study: RoutineItem("Study", 25)
}

val items = listOf(
    RoutineItem.Skincare,
    RoutineItem.Fitness,
    RoutineItem.Yoga,
    RoutineItem.Cleaning,
    RoutineItem.Study
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
            items.forEach { item ->
                RoutineBlock(item = item)
            }
        }
    }
}

@Composable
fun RoutineBlock(item: RoutineItem) {
    Surface() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.progress == 100,
                onCheckedChange = {
                    item.completeRoutine()
                }
            )
            Column {
                Text(item.title)
                LinearProgressIndicator(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .clip(CircleShape)
                        .height(8.dp),
                    progress = item.progress/100f,
                    color = getColour(item.progress/100f),
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
    RoutineBlock(items[0])
}