package com.example.goosebuddy.ui.screens

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable


sealed class RoutineItem(var title: String, var progress: Int) {
    object Skincare: RoutineItem("Skincare", 100)
    object Fitness: RoutineItem("Fitness", 75)
    object Yoga: RoutineItem("Yoga", 0)
    object Cleaning: RoutineItem("Cleaning", 50)
    object Study: RoutineItem("Study", 25)
}

@Composable
fun Routines() {
    Surface() {

    }
}

fun RoutineBlock() {

}