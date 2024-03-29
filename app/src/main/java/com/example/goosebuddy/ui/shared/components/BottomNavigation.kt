package com.example.goosebuddy.ui.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.goosebuddy.R
import com.example.goosebuddy.models.BottomNavigationItem
import com.example.goosebuddy.ui.theme.*

val items = listOf(
    BottomNavigationItem.DailyRoutines,
    BottomNavigationItem.Habits,
    BottomNavigationItem.Home,
    BottomNavigationItem.Calendar,
    BottomNavigationItem.Profile
)

private val ICON_SIZE = 40.dp

@Composable
fun BottomNavigation(
    navController: NavController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White)
            .height(ICON_SIZE.times(1.5f))

    ) {
        items.forEach { item ->
            IconButton(
                onClick = { navController.navigate(item.screen_route) },
                modifier = getModifier(item, navController)
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(ICON_SIZE),
                    tint = item.color,
                )
            }
        }
    }
}

@Composable
fun getModifier(item: BottomNavigationItem, navController: NavController): Modifier {
    val isHomeButton = item.title == "Home"
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var currentDestination = navBackStackEntry?.destination?.route.toString()
    if (currentDestination.contains("/")) {
        currentDestination = currentDestination.split("/")[0]
    }
    val homeModifier = Modifier
        .background(LightBlue, RoundedCornerShape(18.dp))
        .width(65.dp)
        .height(60.dp)

    val defaultModifier = Modifier
        .size(ICON_SIZE)

    val selectedModifier = Modifier
        .drawBehind {
            val strokeWidth = Stroke.DefaultMiter + 10f
            val y = size.height - strokeWidth / 2

            drawLine(
                Color.LightGray,
                Offset(0f, y),
                Offset(size.width, y),
                strokeWidth
            )
        }
    val homeSelectedModifier = Modifier
        .border(4.dp, LightGrey, RoundedCornerShape(18.dp))
    var modifier: Modifier =  if (isHomeButton) homeModifier else defaultModifier

    if (currentDestination == item.screen_route) {
        modifier = if (item.title !== "Home") {
            modifier.then(selectedModifier)
        } else {
            modifier.then(homeSelectedModifier)
        }

    }
    return modifier
}

@Preview
@Composable
fun BottomNavigationPreview() {
    //BottomNavigation()
}
