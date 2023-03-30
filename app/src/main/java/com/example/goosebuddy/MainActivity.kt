package com.example.goosebuddy

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.goosebuddy.ui.screens.*
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigation
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.shared.components.topbar.TopBar
import com.example.goosebuddy.ui.theme.GooseBuddyTheme
import com.example.goosebuddy.ui.theme.Grey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootNavigationGraph()
        }
    }
}

@Composable
fun MainFoundation(navController: NavHostController, scaffoldState: ScaffoldState, content: @Composable() () -> Unit) {
    GooseBuddyTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                Text("hello this is a drawer")
            },
            topBar = { TopBar(scaffoldState) },
            bottomBar = { BottomNavigation(navController = navController) }
        ) { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .fillMaxHeight()
                    .background(Grey)
            ) {
                content()
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
    NavHost(
        navController = navController,
        startDestination = "routines",
        route = "main"
    ) {
        composable(BottomNavigationItem.Home.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Greeting(name = "home")
            }
        }
        composable(BottomNavigationItem.Habits.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Habits(navController = navController, db = db)
            }
        }
        composable(BottomNavigationItem.DailyRoutines.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Routines(navController = navController)
            }
        }
        composable("routines/{routine_id}") {
            val subroutines = arrayOf(
                Subroutine(name = "part 1", description = "aaa", completed = true),
                Subroutine(name = "part 2", description = "aaa", completed = true),
                Subroutine(name = "part 3", description = "aaa", completed = false),
                Subroutine(name = "part 4", description = "aaa", completed = false),
                Subroutine(name = "part 5", description = "aaa", completed = true),
            )
            Routine(name = "Morning Routine", subroutines = subroutines)
        }
        composable(BottomNavigationItem.Calendar.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Greeting(name = "calendar")
            }
        }
        composable(BottomNavigationItem.Profile.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Greeting(name = "profile")
            }
        }
        composable(
            "onboarding"
        ) {
            OnboardingFlow(navController = navController, "welcome")
        }
        composable(
            "onboarding/{step}",
            arguments = listOf(navArgument("step") { type = NavType.StringType })
        ) { backStackEntry ->
            OnboardingFlow(navController = navController, backStackEntry.arguments?.getString("step"))
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