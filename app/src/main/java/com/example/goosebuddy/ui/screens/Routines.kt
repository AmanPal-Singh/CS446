package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


sealed class RoutineItem(var title: String, var progress: Int) {
    object Skincare: RoutineItem("Skincare", 100)
    object Fitness: RoutineItem("Fitness", 75)
    object Yoga: RoutineItem("Yoga", 0)
    object Cleaning: RoutineItem("Cleaning", 50)
    object Study: RoutineItem("Study", 25)
}

val items = listOf(
    RoutineItem.Skincare
)

@Composable
fun Routines() {
    Surface() {

    }
}

@Composable
fun RoutineBlock(item: RoutineItem) {
    Surface() {
        Row() {
            
        }
    }
}

@Preview
@Composable
fun RoutineBlockPreview() {
    RoutineBlock(items[0])
}