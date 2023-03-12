package com.example.goosebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigation
import com.example.goosebuddy.ui.theme.GooseBuddyTheme
import com.example.goosebuddy.ui.theme.LightGrey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GooseBuddyTheme {
                Scaffold(
                    bottomBar = { BottomNavigation() }
                ) { padding ->
                    Surface(
                        modifier = Modifier.padding(padding),
                        color = LightGrey
                    ) {
                        Greeting(name = "Student")
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name! I am your buddy")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GooseBuddyTheme {
        Greeting("Waterloo")
    }
}