package com.example.goosebuddy.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.ui.shared.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddHabit(scope: CoroutineScope, sheetState: ModalBottomSheetState, db: AppDatabase, onHabitChange: () -> Unit) {
    var habitsDao = db.habitsDao()

    var habitName by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var habitDescription by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var habitCompletionSteps by remember {
        mutableStateOf(TextFieldValue("1"))
    }

    var expanded by remember { mutableStateOf(false) }
    Column {
        SpeechBubble("Honk! Adding a habit...")
        ClipboardAccessory(HoldingGoose()).decorate()

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
                    label = { Text(text = "Times Daily") },
                )
                Button(
                    onClick = {
                        scope.launch {
                            // Insert habit
                            var num = habitCompletionSteps.text.toInt()
                            habitsDao.insertAll(Habits(0, habitName.text, habitDescription.text, 0, "Daily", completionSteps = num))
                            // Reset form
                            habitName = TextFieldValue("")
                            habitDescription = TextFieldValue("")
                            habitCompletionSteps = TextFieldValue("1")
                            sheetState.hide()
                            onHabitChange()
                        }
                    },
                ) {
                    Text("Add")
                }
            }
        }
    }

}


