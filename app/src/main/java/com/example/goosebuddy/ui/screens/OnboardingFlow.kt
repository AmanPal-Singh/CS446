package com.example.goosebuddy.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.goosebuddy.ui.theme.*
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.models.UserData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.shared.components.SpeechBubble

class OnboardingStep(
    var name: String
)

val suggestedHabit = mapOf(
    "hasRoommates" to listOf(
        Habits(0, "Chore schedule", "Set up a chore schedule with your roommates and stick with it!", schedule = "Weekly"),
        Habits(0, "Hang out with roommates", "It's important to have a good relationship with your roommates!", schedule = "Weekly"),
    ),
    "onStudentRes" to listOf(
        Habits(0, "Do Laundry", "Doing laundry is an important task for your Hygiene!", schedule = "Weekly"),
        Habits(0, "Shower", "Being clean is an important for your (and your friends') health!"),
        ),
    "firstTimeAlone" to listOf(
        Habits(0, "Call your parents", "Your parents miss you! Let them know how you are doing!"),
        Habits(0, "Go on a walk", "Get your daily steps in!"),
    )
)

val onboardingSteps = arrayOf(
    OnboardingStep("welcome"),
    OnboardingStep("name"),
    OnboardingStep("wat"),
    OnboardingStep("year"),
    OnboardingStep("residence"),
    OnboardingStep("submit"),
    OnboardingStep("recommendations"),
    OnboardingStep("schedule"),
)

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
    step: MutableState<Int>,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            modifier = Modifier.padding(50.dp),
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            val buttonMsg = when (onboardingSteps[step.value].name) {
                "welcome" -> "Let's go!"
                "name" -> "Done!"
                "wat" -> "Next"
                "year" -> "Done!"
                "residence" -> "Got it!"
                "submit" -> "Yay! Done!"
                "recommendations" -> "Let's get started!"
                "schedule" -> "Show me the app!"
                else -> "Next!"
            }
            Text(buttonMsg)
        }
    }
}

@Composable
fun OnboardingFlow(navController: NavHostController, db: AppDatabase, cvm: CalendarViewModel, firstStep: Int) {
    // the user data collected throughout the onboarding process
    // will be added to the database after user submits
    // will be used to make recommendations to users
    val userData = remember { mutableStateOf(UserData(firstStep)) }

    // which step we are currently are
    // default to starting from first step: 0
    val step = remember { mutableStateOf(0) }

    GooseBuddyTheme {
        Scaffold(
            topBar = { ProgressIndicator(completed = step.value + 1, total = onboardingSteps.size) }
        ) { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .fillMaxHeight()
            ) {
                // the onboarding content shown to user based on which step
                OnboardingStepComponent(
                    userData,
                    step,
                    navController = navController,
                    db = db,
                    cvm = cvm
                )

                // the buttons for navigation throughout onboarding
                BottomButtons(
                    step = step,
                    onClick = {
                        // navigate to next onboarding step if not last step
                        if (step.value == onboardingSteps.size - 1) {
                            // navigate out of onboarding - to lock
                            navController.navigate("lock")
                        } else {
                            step.value += 1
                        }

                        // add userData to db and make recommendations when on submit step
                        if (onboardingSteps[step.value].name == "submit") {
                            println("user data")
                            println(userData.value)

                            // add userData to db
                            val userDataDao = db.userdataDao()
                            userDataDao.insert(userData.value)

                            // make recommendations based on user data
                            val habitsDao = db.habitsDao()
                            habitsDao.deleteAll()

                            if (userData.value.hasRoommates) {
                                for( habits in suggestedHabit["hasRoommates"]!!) {
                                    habitsDao.insertAll(habits)
                                }
                            }
                            if (userData.value.onStudentRes) {
                                for( habits in suggestedHabit["onStudentRes"]!!) {
                                    habitsDao.insertAll(habits)
                                }
                            }
                            if (userData.value.firstTimeAlone) {
                                for( habits in suggestedHabit["firstTimeAlone"]!!) {
                                    habitsDao.insertAll(habits)
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun OnboardingStepComponent(
    userData: MutableState<UserData>,
    step: MutableState<Int>,
    navController: NavHostController,
    db: AppDatabase,
    cvm: CalendarViewModel,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = Beige)
            .padding(50.dp),
    ) {
        // determine what content to show user based on which step
        val onboardingStep = onboardingSteps[step.value]
        when (onboardingStep.name) {
            "welcome" -> WelcomePage()
            "name" -> NamePage(userData)
            "wat" -> WatPage(userData)
            "year" -> YearPage(userData)
            "residence" -> ResidencePage(userData)
            "submit" -> SubmitPage()
            "recommendations" -> RecommendationsPage(db)
            "schedule" -> SchedulePage(navController, cvm)
        }
    }
}


@Composable
fun WelcomePage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val welcomeMsg = "Welcome to GooseBuddy!\nAre you ready to get started on your journey?"
        SpeechBubble(welcomeMsg)
        Goose(size=200.dp)
    }
}

@Composable
fun NamePage(userData: MutableState<UserData>) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val nameMsg = "My name is Mr. Goose!\nWhat's your name?"
        SpeechBubble(nameMsg)
        Goose(size=200.dp)

        // text input field for name
        var text = remember { mutableStateOf("") }
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
                userData.value = userData.value.copy(name = it)
            },
            label = { Text(text = "Name") },
            placeholder = { Text(text = "Enter your name") },
        )
    }
}

@Composable
fun WatPage(userData: MutableState<UserData>) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val watMsg = "Hey ${userData.value.name}! Enter your WAT number below.\nYou can find it on your WAT card."
        SpeechBubble(watMsg)
        Goose(size=200.dp)

        // text input field for name
        var text = remember { mutableStateOf("") }
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
                // update model
                userData.value = userData.value.copy(wat = it.toIntOrNull() ?: 0)
            },
            label = { Text(text = "WAT number") },
            placeholder = { Text(text = "Enter your WAT number") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}



@Composable
fun YearPage(userData: MutableState<UserData>) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val yearMsg = "What year are you in?"
        SpeechBubble(yearMsg)
        Goose(size=200.dp)

        // text input field for name
        var text = remember { mutableStateOf("") }
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
                // update model
                userData.value = userData.value.copy(year = it.toIntOrNull() ?: 0)
            },
            label = { Text(text = "Year") },
            placeholder = { Text(text = "Enter your year as an integer") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}


@Composable
fun ResidencePage(userData: MutableState<UserData>) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val residenceMsg = "Describe your residence situation"
        SpeechBubble(residenceMsg)
        Goose(size=200.dp)

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

            Row (
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = livingSituations.getValue(it)
                )
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked_ ->
                        isChecked = checked_
                        // update model
                        when (it) {
                            "roommates" -> userData.value = userData.value.copy(hasRoommates = checked_)
                            "student_res" -> userData.value = userData.value.copy(onStudentRes = checked_)
                            "first_time" -> userData.value = userData.value.copy(firstTimeAlone = checked_)
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Green
                    )
                )
            }
        }
    }
}

@Composable
fun SubmitPage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val submitMsg = "Congrats, you are all set!\nSubmit whenever you are ready!"
        SpeechBubble(submitMsg)
        Goose(size=200.dp)
    }
}

@Composable
fun RecommendationsPage(db: AppDatabase,) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val habitsDao = db.habitsDao()
        val habits = habitsDao.getAll()
        val habitsMsg = habits.joinToString(", ") { "\"${it.title}\"" }

        val recomendationMsg = "We have made a few recommendations based on your submission!\nWe have added ${habitsMsg}! You can review them in Habits!"
        SpeechBubble(recomendationMsg)
        Goose(size=200.dp)
    }
}

@Composable
fun SchedulePage(navController: NavHostController, cvm: CalendarViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val residenceMsg = "One more thing! You can import your schedule if you like!"
        Text(residenceMsg, textAlign = TextAlign.Center)

        fun onImportSchedule(subject: String, courseNumber: String, classNumber: String) {
            cvm.importSchedule(subject, courseNumber, classNumber)

            // navigate back to schedule page
            navController.navigate("onboarding/schedule")
        }

        val sivm = ScheduleImportViewModel()
        ScheduleImport(sivm = sivm, onSubmit = ::onImportSchedule)
    }
}

