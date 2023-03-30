package com.example.goosebuddy

import android.content.Context
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
import androidx.room.Room
import com.example.goosebuddy.ui.screens.Routines
import com.example.goosebuddy.ui.screens.Calendar
import com.example.goosebuddy.ui.screens.Habits
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigation
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.shared.components.topbar.TopBar
import com.example.goosebuddy.ui.theme.GooseBuddyTheme
import com.example.goosebuddy.ui.theme.Grey
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState

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
                       NavigationGraph(navController = navController, context=applicationContext )
                    }
                }
            }
        }
    }
}

@Composable
fun RootNavigationGraph() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    var db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    ).allowMainThreadQueries().fallbackToDestructiveMigrationFrom(1).build()
    NavHost(navController, startDestination = BottomNavigationItem.Home.screen_route) {
        composable(BottomNavigationItem.Home.screen_route) {
            Greeting(name = "Home")
        }
        composable(BottomNavigationItem.DailyRoutines.screen_route) {
            Routines(navController = navController)
        }
        composable(BottomNavigationItem.Habits.screen_route) {
            Habits(navController = navController, db = db)
        }
        composable(BottomNavigationItem.Calendar.screen_route) {
            Calendar(calendarState = calendarState)
        }
        composable(BottomNavigationItem.Profile.screen_route) {
            Greeting(name = "Profile")
        }
        composable("routine1") {
            Greeting(name = "more on a routine")
        }
     }
}

@Composable
fun Greeting(name: String) {
    Text(text = "$name")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GooseBuddyTheme {
        Greeting("Waterloo")
    }
}