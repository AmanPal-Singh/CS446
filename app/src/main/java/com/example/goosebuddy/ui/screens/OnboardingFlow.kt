package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.goosebuddy.ui.theme.GooseBuddyTheme
import com.example.goosebuddy.ui.theme.Green
import com.example.goosebuddy.ui.theme.Grey
import java.lang.Integer.min

@Composable
fun OnboardingFlow(navController: NavHostController, step: String?) {
    val onboardingStep = onboardingSteps.find { s -> s.name == step }
    val completed = onboardingSteps.indexOf(onboardingStep) + 1
    GooseBuddyTheme {
        Scaffold(
            topBar = { ProgressIndicator(completed = completed, total = onboardingSteps.size) }
        ) { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .fillMaxHeight()
            ) {
                if (onboardingStep != null) {
                    OnboardingStepComponent(
                        step = onboardingStep,
                        navController = navController,
                    )
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
fun OnboardingStepComponent(
    step: OnboardingStep,
    navController: NavHostController,
) {
    Column(
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
                val progress = onboardingSteps.indexOf(step)
                println(progress)
                println(onboardingSteps.size - 1)
                if (progress == onboardingSteps.size - 1) {
                    println("hellloooo")
                    navController.navigate("home")
                } else {
                    val step = onboardingSteps[min(
                        progress + 1,
                        onboardingSteps.size - 1
                    )].name
                    navController.navigate("onboarding/$step")
                }
            },
            skippable = step.skippable
        )
    }
}

@Composable
fun ProgressIndicator(completed: Int, total: Int) {
    Row(
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