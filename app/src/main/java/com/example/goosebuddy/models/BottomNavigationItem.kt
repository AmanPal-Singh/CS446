package com.example.goosebuddy.models

import androidx.compose.ui.graphics.Color
import com.example.goosebuddy.R
import com.example.goosebuddy.ui.theme.*

sealed class BottomNavigationItem(var title: String, var icon: Int, var color: Color, var screen_route: String) {
    object Home: BottomNavigationItem("Home", R.drawable.home, White, "home")
    object DailyRoutines: BottomNavigationItem("Routines", R.drawable.access_time,  Yellow, "routines")
    object Habits: BottomNavigationItem("Habits",  R.drawable.check_circle_outline, Green, "habits")
    object Calendar: BottomNavigationItem("Calendar", R.drawable.calendar_today, Red, "calendar")
    object Profile: BottomNavigationItem("Profile", R.drawable.person, Beige,"profile")
}