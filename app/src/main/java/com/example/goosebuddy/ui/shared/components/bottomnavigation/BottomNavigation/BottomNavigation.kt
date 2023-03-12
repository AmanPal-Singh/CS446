package com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.goosebuddy.ui.theme.*
import java.lang.reflect.Modifier


sealed class BottomNavigationItem(var title: String, var icon: ImageVector, var color: Color, var screen_route: String) {
    object Home: BottomNavigationItem("Home", Icons.Filled.Home, Yellow, "")
    object DailyRoutines: BottomNavigationItem("Daily Routines",Icons.Outlined.Check,  Green, "")
    object Habits: BottomNavigationItem("Habits", Icons.Outlined.Favorite, White, "")
    object Calendar: BottomNavigationItem("Calendar", Icons.Outlined.DateRange, Red, "")
    object Profile: BottomNavigationItem("Profile", Icons.Filled.Person, Beige,"")

}

val items = listOf(
    BottomNavigationItem.DailyRoutines,
    BottomNavigationItem.Habits,
    BottomNavigationItem.Home,
    BottomNavigationItem.Calendar,
    BottomNavigationItem.Profile
)

@Composable
fun BottomNavigation() {
    androidx.compose.material.BottomNavigation(
        backgroundColor = Red,
        contentColor = Grey,
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                selected = false,
                label = { Text(item.title) },
                icon = {
                    androidx.compose.material.Icon(imageVector = item.icon, contentDescription = item.title)
                },
                onClick = { /*TODO*/ }
            )
        }
    }
}