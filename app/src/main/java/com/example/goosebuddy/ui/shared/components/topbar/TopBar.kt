package com.example.goosebuddy.ui.shared.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.White
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun TopBar(scaffoldState: ScaffoldState, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    var includeBackBtn = remember { mutableStateOf(false) }
    var title = navController.currentDestination?.route.toString().replaceFirstChar{ it.uppercaseChar() }
    if (title.contains("/")) {
        includeBackBtn.value = true
    }
    title = title.split("/")[0]
    if (title == "Home") {
        return
    }

    Card(
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(White)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    if (includeBackBtn.value) {
                        navController.navigateUp()
                    }
                }
            ) {
                if (includeBackBtn.value ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Transparent
                    )
                }
            }
            Text(title, fontSize = 24.sp)
            IconButton(
                onClick = {
                    if (includeBackBtn.value) {
                        navController.navigateUp()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Transparent
                )
            }
        }
    }

}