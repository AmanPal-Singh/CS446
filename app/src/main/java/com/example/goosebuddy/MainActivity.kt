package com.example.goosebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.goosebuddy.ui.screens.Routines
import com.example.goosebuddy.ui.screens.Calendar
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigation
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.shared.components.topbar.TopBar
import com.example.goosebuddy.ui.theme.GooseBuddyTheme
import com.example.goosebuddy.ui.theme.Grey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            GooseBuddyTheme {
                Scaffold(
                    topBar = { TopBar() },
                    bottomBar = { BottomNavigation(navController = navController) }
                ) { padding ->
                    Surface(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .fillMaxHeight()
                            .background(Grey)
                    ) {
                       NavigationGraph(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    //var calendarState = rememberSelectableCalendarState()
    NavHost(navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingActivity()
        }
        composable(BottomNavigationItem.Home.screen_route) {
            Greeting(name = "Home")
        }
        composable(BottomNavigationItem.DailyRoutines.screen_route) {
            Routines()
        }
        composable(BottomNavigationItem.Habits.screen_route) {
            Greeting(name = "Habits")
        }
        composable(BottomNavigationItem.Calendar.screen_route) {
            Calendar()
        }
        composable(BottomNavigationItem.Profile.screen_route) {
            Greeting(name = "Profile")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "hello $name")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GooseBuddyTheme {
        Greeting("Waterloo")
    }
}