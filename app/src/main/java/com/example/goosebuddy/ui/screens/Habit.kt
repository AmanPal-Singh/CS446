package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.RoomDatabase
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.Black
import com.example.goosebuddy.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun Habit(habitId: Int, db: AppDatabase, navController: NavController) {
    // Query the habit we are editing.
    var habitsDao = db.habitsDao()
    var habit = habitsDao.loadSingle(habitId)

    // Form values
    var habitName by remember { mutableStateOf(TextFieldValue(habit.title)) }
    var habitDescription by remember { mutableStateOf(TextFieldValue(habit.description)) }
    var habitSchedule by remember { mutableStateOf(TextFieldValue(habit.schedule)) }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = habitName,
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
            onValueChange = {
                habitName = it
            },
            label = { Text(text = "Habit Name") },
            placeholder = { Text(text = "Update the habit name") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor= White
            )
        )
        OutlinedTextField(
            value = habitDescription,
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
            onValueChange = {
                habitDescription = it
            },
            label = { Text(text = "Habit Description") },
            placeholder = { Text(text = "Update the habit description") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor= White
            )
        )
        Button(
            onClick = {
                habitsDao.update(
                    com.example.goosebuddy.models.Habits(
                        habit.id,
                        habitName.text,
                        habitDescription.text,
                        0,
                        "Daily"
                    )
                )
                navController.navigate(BottomNavigationItem.Habits.screen_route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Black)
        )
        {
            Text(text="Update Habit", color = White)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpdateHabit(scope: CoroutineScope, sheetState: ModalBottomSheetState, db: AppDatabase, navController: NavController, habitId: Int) {
    var habitsDao = db.habitsDao()
    var habit = habitsDao.loadSingle(habitId)

    // Form values
    var habitName by remember { mutableStateOf(TextFieldValue(habit.title)) }
    var habitDescription by remember { mutableStateOf(TextFieldValue(habit.description)) }

    var schedule by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var expanded by remember { mutableStateOf(false) }
    Column {
        SpeechBubble("Honk! Updating the habit $habitName...")
        Goose(200.dp, 8f)
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = habitName,
                    onValueChange = { newText ->
                        habitName = newText
                    },
                    label = { Text(text = "Name") },
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = habitDescription,
                    onValueChange = { newText ->
                        habitDescription = newText
                    },
                    label = { Text(text = "Description") },
                )
                Button(onClick = { scope.launch {
                    // Update habit
                    habitsDao.update(
                        com.example.goosebuddy.models.Habits(
                            habit.id,
                            habitName.text,
                            habitDescription.text,
                            0,
                            "Daily"
                        )
                    )
                    sheetState.hide()
                    navController.navigate(BottomNavigationItem.Habits.screen_route)
                }  }) {
                    Text("Add")
                }
            }
        }
    }

}
