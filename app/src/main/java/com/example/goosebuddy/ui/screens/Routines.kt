package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.tooling.preview.Preview
import com.example.goosebuddy.ui.theme.Grey


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
                LinearProgressIndicator(progress = item.progress/100f)
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
    RoutineBlock(items[1])
}