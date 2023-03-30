package com.example.goosebuddy.ui.shared.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.goosebuddy.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun TopBar(scaffoldState: ScaffoldState) {
    val scope = rememberCoroutineScope()
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
            .background(White)
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    scaffoldState.drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
        }
    }
}