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
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.Black
import com.example.goosebuddy.ui.theme.White


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
                println(habit.id)
                println(habitName.text)
                println(habit.title)
                habitsDao.update(
                    com.example.goosebuddy.models.Habits(
                        habit.id,
                        habitName.text,
                        habitDescription.text,
                        0,
                        "Daily"
                    )
                )
                println(habit.title)
                navController.navigate(BottomNavigationItem.Habits.screen_route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Black)
        )
        {
            Text(text="Update Habit", color = White)
        }
    }
}
