package com.example.goosebuddy.ui.screens
import com.example.goosebuddy.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.goosebuddy.ui.theme.*
import java.lang.Integer.min
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.UserData
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem

@Composable
fun OnboardingFlow(navController: NavHostController, db: AppDatabase, cvm: CalendarViewModel, step: String?) {
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
                        db = db,
                        cvm = cvm
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
    OnboardingStep("year", false),
    OnboardingStep("residence", true),
    OnboardingStep("schedule", true),
    OnboardingStep("submit", false)
)

@Composable
fun OnboardingStepComponent(
    step: OnboardingStep,
    navController: NavHostController,
    db: AppDatabase,
    cvm: CalendarViewModel
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Beige)
    ) {

        val userData = UserData()
        when (step.name) {
            "welcome" -> WelcomePage(userData)
            "name" -> NamePage(userData)
            "year" -> YearPage(userData)
            "residence" -> ResidencePage(userData)
            "schedule" -> SchedulePage(userData, navController, cvm)
            "submit" -> SubmitPage(userData)
        }

        BottomButtons(
            buttonText = if (step.name == "submit") "Submit" else "Next",
            onClick = {
                val progress = onboardingSteps.indexOf(step)
                // if user is on the last step and clicks submit
                if (progress == onboardingSteps.size - 1) {
                    // save all the user data into dao
                    var userdataDao = db.userdataDao()
                    userdataDao.insertAll(userData)
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
            Button(onClick = {}) {
                Text(text = "Skip for now")
            }
        }
    }
}



@Composable
fun WelcomePage(userData: UserData) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val imageModifier = Modifier
            .size(150.dp)
        Image(
            painter = painterResource(id = R.drawable.onboarding_goose),
            contentDescription = "Goose Image",
            contentScale = ContentScale.Fit,
            modifier = imageModifier
        )
        val welcomeMsg = "Welcome to GooseBuddy!\nAre you ready to get started on your journey?"
        Text(welcomeMsg, textAlign = TextAlign.Center)
    }
}

@Composable
fun NamePage(userData: UserData) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val nameMsg = "What's your name?"
        Text(nameMsg, textAlign = TextAlign.Center)

        // text input field for name
        var text = remember { mutableStateOf("") }
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
                // update model
                userData.name = it
            },
            label = { Text(text = "Name") },
            placeholder = { Text(text = "Enter your name") },
        )
    }
}

@Composable
fun YearPage(userData: UserData) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val nameMsg = "What year are you in?"
        Text(nameMsg, textAlign = TextAlign.Center)

        // text input field for name
        var text = remember { mutableStateOf("") }
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
                // update model
                userData.year = it.toInt()
            },
            label = { Text(text = "Year") },
            placeholder = { Text(text = "Enter your year as an integer") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}


@Composable
fun ResidencePage(userData: UserData) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val residenceMsg = "Describe your residence situation"
        Text(residenceMsg, textAlign = TextAlign.Center)

        val livingSituations = mapOf(
            "roommates" to "I have roommates.",
            "student_res" to "I live in student residence.",
            "first_time" to "First time living alone",
        )

        livingSituations.keys.forEach {
            // checkbox state
            var isChecked by remember {
                mutableStateOf(false)
            }

            Row {
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = livingSituations.getValue(it)
                )
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked_ ->
                        isChecked = checked_
                        // update model
                        if (it == "roommates") {
                            userData.hasRoommates = checked_
                        } else if (it == "student_res") {
                            userData.onStudentRes = checked_
                        } else if (it == "first_time") {
                            userData.firstTimeAlone = checked_
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Yellow
                    )
                )
            }
        }
    }
}

@Composable
fun SchedulePage(userData: UserData, navController: NavHostController, cvm: CalendarViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val residenceMsg = "Import your schedule"
        Text(residenceMsg, textAlign = TextAlign.Center)

        fun onImportSchedule(subject: String, courseNumber: String, classNumber: String) {
            cvm.importSchedule(subject, courseNumber, classNumber)

            // navigate back to onboarding page
            navController.navigate("onboarding/schedule")
        }

        val sivm = ScheduleImportViewModel()
        ScheduleImport(sivm = sivm, onSubmit = ::onImportSchedule)
    }
}


@Composable
fun SubmitPage(userData: UserData) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val imageModifier = Modifier
            .size(150.dp)
        Image(
            painter = painterResource(id = R.drawable.onboarding_goose),
            contentDescription = "Goose Image",
            contentScale = ContentScale.Fit,
            modifier = imageModifier
        )
        val submitMsg = "Congrats, you are all set!\n Submit whenever you are ready!"
        Text(submitMsg, textAlign = TextAlign.Center)
    }
}