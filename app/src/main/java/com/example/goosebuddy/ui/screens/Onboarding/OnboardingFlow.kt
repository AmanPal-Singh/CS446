package com.example.goosebuddy.ui.screens.Onboarding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.goosebuddy.models.*
import androidx.lifecycle.viewModelScope
import com.example.goosebuddy.ui.screens.CalendarViewModel
import com.example.goosebuddy.ui.screens.Home.HomeViewModel
import com.example.goosebuddy.ui.screens.ScheduleImport
import com.example.goosebuddy.ui.screens.ScheduleImportViewModel
import com.example.goosebuddy.ui.shared.components.*
import kotlinx.coroutines.launch

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
    onClick: () -> Unit,
    onboardingViewModel: OnboardingViewModel
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Beige)
    ) {
        Button(
            modifier = Modifier.padding(50.dp),
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            val buttonMsg = when (onboardingViewModel.onboardingSteps[step.value].name) {
                "welcome" -> "Let's go!"
                "name" -> "Next"
                "wat" -> "Next"
                "year" -> "Done!"
                "residence" -> "Got it!"
                "submit" -> "Let's get started!"
                "recommendations" -> "Let's get started!"
                "schedule" -> "Next"
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

    val viewModel by remember {
        mutableStateOf(OnboardingViewModel(db))
    }

    val userData = remember { mutableStateOf(UserData(firstStep)) }

    // which step we are currently are
    // default to starting from first step: 0
    val step = remember { mutableStateOf(0) }

    GooseBuddyTheme {
        Scaffold(
            topBar = { ProgressIndicator(completed = step.value + 1, total = viewModel.onboardingSteps.size) },
            bottomBar = {
                BottomButtons(
                    step = step,
                    onClick = {
                        // navigate to next onboarding step if not last step
                        if (step.value == viewModel.onboardingSteps.size - 1) {
                            // navigate out of onboarding - to lock
                            navController.navigate("lock")
                        } else {
                            step.value += 1
                        }

                        // add userData to db and make recommendations when on submit step
                        if (viewModel.onboardingSteps[step.value].name == "submit") {

                            // add userData to db
                            viewModel.insertUserData(userData.value)

                            // make recommendations based on user data
                            viewModel.clearHabits()

                            if (userData.value.hasRoommates) {
                                for( habits in viewModel.getSuggestedHabit("hasRoommates")!!) {
                                    viewModel.insertHabits(habits)
                                }
                            }
                            if (userData.value.onStudentRes) {
                                for( habits in viewModel.getSuggestedHabit("onStudentRes")!!) {
                                    viewModel.insertHabits(habits)
                                }
                            }
                            if (userData.value.firstTimeAlone) {
                                for( habits in viewModel.getSuggestedHabit("firstTimeAlone")!!) {
                                    viewModel.insertHabits(habits)
                                }
                            }

                            // add pomodoro for everyone
                            viewModel.createPomodoroSubroutines()
                        }
                    },
                    onboardingViewModel = viewModel
                )
            }
        ) { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .fillMaxHeight()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Beige)
                ) {
                    OnboardingStepComponent(
                        userData,
                        step,
                        navController = navController,
                        db = db,
                        cvm = cvm,
                        ovm = viewModel
                    )
                }
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
    ovm: OnboardingViewModel,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(50.dp)
    ) {
        // determine what content to show user based on which step
        val onboardingStep = ovm.onboardingSteps[step.value]
        when (onboardingStep.name) {
            "welcome" -> WelcomePage(ovm)
            "name" -> NamePage(userData)
            "wat" -> WatPage(userData)
            "year" -> YearPage(userData)
            "residence" -> ResidencePage(userData)
            "schedule" -> SchedulePage(navController, cvm)
            "recommendations" -> RecommendationsPage(db, ovm)
            "submit" -> SubmitPage()
        }
        // the buttons for navigation throughout onboarding
    }
}


@Composable
fun WelcomePage(onboardingViewModel: OnboardingViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        SpeechBubble(onboardingViewModel.getWelcomeMessage(), includeLeftSpacing = false)
        WavingGoose().decorate()
    }
}

@Composable
fun NamePage(userData: MutableState<UserData>) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val nameMsg = "My name is Mr. Goose!\nWhat's your name?"
        SpeechBubble(nameMsg, includeLeftSpacing = false)
        ClipboardAccessory(HoldingGoose()).decorate()

        // text input field for name
        var text = remember { mutableStateOf("") }
        OutlinedTextField(
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
        SpeechBubble(watMsg, includeLeftSpacing = false)
        ClipboardAccessory(HoldingGoose()).decorate()

        // text input field for name
        var text = remember { mutableStateOf("") }
        OutlinedTextField(
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
        val yearMsg = "What year are you in? \nPlease enter your year as an integer."
        SpeechBubble(yearMsg, includeLeftSpacing = false)
        ClipboardAccessory(HoldingGoose()).decorate()

        // text input field for name
        var text = remember { mutableStateOf("") }
        OutlinedTextField(
            value = text.value,
            onValueChange = {
                text.value = it
                // update model
                userData.value = userData.value.copy(year = it.toIntOrNull() ?: 0)
            },
            label = { Text(text = "Year") },
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
        val residenceMsg = "What is your living situation like? \nPlease select all that apply."
        SpeechBubble(residenceMsg, includeLeftSpacing = false)
        ClipboardAccessory(HoldingGoose()).decorate()

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
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .width(500.dp)
            ) {
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
                Text(
                    modifier = Modifier.padding(start = 1.dp),
                    text = livingSituations.getValue(it)
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
        SpeechBubble(submitMsg, includeLeftSpacing = false)
        HeartAccessory(WavingGoose()).decorate()
    }
}

@Composable
fun RecommendationsPage(db: AppDatabase, onboardingViewModel: OnboardingViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val habits = onboardingViewModel.getHabits()
        val habitsMsg = habits.joinToString(", ") { "\"${it.title}\"" }

        val recomendationMsg = "We have made a few recommendations based on your submission!\n\n\nWe have added ${habitsMsg}! You can review them in Habits!\n\n\nWe also added a pomodoro routine to help you with studying!"
        SpeechBubble(recomendationMsg, includeLeftSpacing = false)
        PencilAccessory(HoldingGoose()).decorate()
    }
}

@Composable
fun SchedulePage(navController: NavHostController, cvm: CalendarViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val residenceMsg = "One more thing! You can import your schedule if you like!"
        SpeechBubble(residenceMsg, includeLeftSpacing = false)
        DefaultGoose().decorate()
        fun onImportSchedule(subject: String, courseNumber: String, classNumber: String) {
            cvm.viewModelScope.launch{
                cvm.importSchedule(subject, courseNumber, classNumber)

                // navigate back to onboarding page
                navController.navigate("onboarding/schedule")
            }
        }

        val sivm = ScheduleImportViewModel()
        ScheduleImport(sivm = sivm, onSubmit = ::onImportSchedule)
    }
}

