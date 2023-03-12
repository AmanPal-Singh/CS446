package com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.goosebuddy.ui.theme.*


sealed class BottomNavigationItem(var title: String, var icon: ImageVector, var color: Color, var screen_route: String) {
    object Home: BottomNavigationItem("Home", Icons.Filled.Home, White, "home")
    object DailyRoutines: BottomNavigationItem("Routines",Icons.Outlined.CheckCircle,  Yellow, "routines")
    object Habits: BottomNavigationItem("Habits", Icons.Outlined.List, Green, "habits")
    object Calendar: BottomNavigationItem("Calendar", Icons.Outlined.DateRange, Red, "calendar")
    object Profile: BottomNavigationItem("Profile", Icons.Filled.Person, Beige,"profile")

}

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
            .padding(vertical = 15.dp)
            .background(color = White)
            .height(ICON_SIZE.times(1.5f))

    ) {
        items.forEach { item ->
                val isHomeButton = item.title == "Home"
                val homeModifier = Modifier
                    .background(LightBlue, RoundedCornerShape(18.dp))
                    .width(65.dp)
                    .height(60.dp)
                val defaultModifier = Modifier.size(ICON_SIZE)

                IconButton(
                    onClick = {
                        navController.navigate(item.screen_route)
                    },
                    modifier = if (isHomeButton) homeModifier else defaultModifier
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(ICON_SIZE),
                        tint = item.color,
                    )
                }
        }
    }
}

@Preview
@Composable
fun BottomNavigationPreview() {
    //BottomNavigation()
}
