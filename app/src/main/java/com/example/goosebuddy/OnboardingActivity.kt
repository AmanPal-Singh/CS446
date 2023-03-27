package com.example.goosebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.goosebuddy.ui.theme.GooseBuddyTheme

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          val navController = rememberNavController()
          GooseBuddyTheme {
              Scaffold() { padding ->
                  Surface(
                      modifier = Modifier
                          .padding(padding)
                  ) {

                  }
              }
          }
        }
    }
}