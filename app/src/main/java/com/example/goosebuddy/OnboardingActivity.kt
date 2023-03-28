package com.example.goosebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.goosebuddy.ui.theme.GooseBuddyTheme
import com.example.goosebuddy.ui.theme.Green
import com.example.goosebuddy.ui.theme.Grey
import java.lang.Integer.min
import kotlin.math.max

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val progress = remember {
                mutableStateOf(0)
            }
            GooseBuddyTheme {
                Scaffold(
                    topBar = { ProgressIndicator(completed = progress.value, total = onboardingSteps.size) }
                ) { padding ->
                    Surface(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .fillMaxHeight()
                    ) {
                        OnboardingNavigationGraph(navController = navController, progress = progress)
                    }
                }
            }
        }
    }
}

class OnboardingStep(
    var name: String,
    var skippable: Boolean,
)

val onboardingSteps = arrayOf(
    OnboardingStep("welcome", false),
    OnboardingStep("name", false),
    OnboardingStep("residence", true),
    OnboardingStep("schedule", true)
)

@Composable
fun OnboardingNavigationGraph(navController: NavHostController, progress: MutableState<Int>) {
    NavHost(navController = navController, startDestination = "welcome", route = "onboarding") {
        onboardingSteps.forEachIndexed {  index, step ->
            composable(step.name) {
                OnboardingStepComponent(
                    step = step,
                    index = index,
                    navController = navController,
                    progress = progress
                )
            }
        }
    }
}

@Composable
fun OnboardingStepComponent(
    step: OnboardingStep,
    index: Int,
    navController: NavHostController,
    progress: MutableState<Int>
) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Text(step.name)
        BottomButtons(
            buttonText = "Next",
            onClick = {
                // If last step
                if (index == onboardingSteps.size - 1) {
                    println("last step")
                    navController.navigate("main")
                }
                navController.navigate(onboardingSteps[min(index+1, onboardingSteps.size - 1)].name)
                progress.value = progress.value + 1
            },
            skippable = step.skippable
        )
    }
}

@Composable
fun ProgressIndicator(completed: Int, total: Int) {
    Row (
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for (i in 1..total) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(10.dp)
                    .clip(RectangleShape)
                    .padding(horizontal = 3.dp)
                    .background(if (i <= completed) Green else Grey)
            )
        }
    }
}

@Composable
fun BottomButtons(
    buttonText: String,
    skippable: Boolean,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { onClick() }
        ) {
            Text("$buttonText")
        }
        if (skippable) {
            Button(onClick = { println("skip") }) {
                Text(text = "Skip for now")
            }
        }
    }
}