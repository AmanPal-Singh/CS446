package com.example.goosebuddy.ui.shared.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.R
import com.example.goosebuddy.ui.theme.Grey

@Composable
fun DeleteButton(onDelete: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { onDelete() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Grey,
        ),
        border = BorderStroke(0.dp, Color.Transparent),
        modifier = modifier
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
    ) {
        Icon(
            painter =  painterResource(id = R.drawable.delete),
            contentDescription = "delete button",
            tint = Grey
        )
    }
}