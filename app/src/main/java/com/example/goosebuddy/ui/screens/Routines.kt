package com.example.goosebuddy.ui.screens

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable


sealed class RoutineItem(var title: String, var progress: Int) {
    object Skincare: RoutineItem("Skincare", 100)
}

@Composable
fun Routines() {
    Surface() {

    }
}

fun RoutineBlock() {

}