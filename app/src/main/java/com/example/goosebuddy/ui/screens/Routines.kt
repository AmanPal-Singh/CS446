package com.example.goosebuddy.ui.screens

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable


sealed class RoutineItem(var title: String, var progress: Int) {
    object Skincare: RoutineItem("Skincare", 100)
    object Skincare: RoutineItem("Fitness", 75)
    object Skincare: RoutineItem("Yoga", 0)
    object Skincare: RoutineItem("Cleaning", 50)
    object Skincare: RoutineItem("Study", 25)
}

@Composable
fun Routines() {
    Surface() {

    }
}

fun RoutineBlock() {

}