package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.ui.screens.Habits.HabitsViewModel
import com.example.goosebuddy.ui.shared.components.*
import com.example.goosebuddy.ui.theme.Green
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpdateHabit(scope: CoroutineScope, sheetState: ModalBottomSheetState, viewModel: HabitsViewModel, onHabitChange: () -> Unit, habitId: Int) {

    var habit = viewModel.querySingle(habitId)

    // Form values
    var habitName by remember { mutableStateOf(TextFieldValue(habit.title)) }
    var habitDescription by remember { mutableStateOf(TextFieldValue(habit.description)) }

    var habitCompletionSteps by remember {
        mutableStateOf(TextFieldValue("${habit.completionSteps}"))
    }

    var expanded by remember { mutableStateOf(false) }
    Column {
        SpeechBubble("Honk! Updating the habit ${habitName.text}...")
        PencilAccessory(HoldingGoose()).decorate()

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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = habitName,
                    onValueChange = { newText ->
                        habitName = newText
                    },
                    label = { Text(text = "Name") },
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = habitDescription,
                    onValueChange = { newText ->
                        habitDescription = newText
                    },
                    label = { Text(text = "Description") },
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = habitCompletionSteps,
                    onValueChange = { newText ->
                        habitCompletionSteps = newText
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    label = { Text(text = "Steps") },
                )
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                    onClick = { scope.launch {
                    // Update habit
                    habit.title = habitName.text
                    habit.description = habitDescription.text
                    habit.completionSteps = habitCompletionSteps.text.toInt()
                    viewModel.updateHabit(habit)
                    sheetState.hide()
                    onHabitChange()
                }  }) {
                    Text("Update Habit")
                }
            }
        }
    }

}
