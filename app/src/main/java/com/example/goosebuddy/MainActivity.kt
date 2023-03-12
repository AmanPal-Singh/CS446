package com.example.goosebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigation
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.Beige
import com.example.goosebuddy.ui.theme.GooseBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GooseBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Waterloo")
                }
            }
        }
    }
}

/**
 * GooseBuddyTheme {
val navController = rememberNavController()
Scaffold(
bottomBar = { BottomNavigation(navController)}
) { padding ->
Surface(
modifier = Modifier
.padding(padding)
) {
Greeting(name = "aaaa")
}
}

}

@Composable
fun NavigationGraph(navController: NavHostController) {

NavHost(navController, startDestination = BottomNavigationItem.Home.screen_route) {
composable(BottomNavigationItem.Home.screen_route) {
println("HELLLO")
Greeting(name = "Home")
}
composable(BottomNavigationItem.DailyRoutines.screen_route) {
Greeting(name = "Daily Routines")
}
composable(BottomNavigationItem.Calendar.screen_route) {
Greeting(name = "Calendar")
}
composable(BottomNavigationItem.Profile.screen_route) {
Greeting(name = "Profile")
}
}
}

 */


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