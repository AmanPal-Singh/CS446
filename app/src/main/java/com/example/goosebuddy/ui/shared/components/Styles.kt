package com.example.goosebuddy.ui.shared.components

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun textFieldStyleBlue() = TextFieldDefaults.textFieldColors(
    focusedIndicatorColor = Color.Blue,
    focusedLabelColor = Color.Blue,
    cursorColor = Color.Blue,
)