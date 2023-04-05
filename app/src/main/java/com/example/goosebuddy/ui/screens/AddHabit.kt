package com.example.goosebuddy.ui.screens


import android.content.Context
import androidx.compose.runtime.getValue

import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goosebuddy.ui.theme.Green
import com.example.goosebuddy.ui.theme.Grey
import com.example.goosebuddy.ui.theme.Red
import com.example.goosebuddy.ui.theme.White
import com.example.goosebuddy.ui.theme.Black
import com.example.goosebuddy.ui.theme.Beige
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import androidx.room.RoomDatabase
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.ui.theme.Yellow
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
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
        SpeechBubble("Honk! Adding a Habit...")
        Goose(size = 200.dp, rotationZ = 8f)
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
                TextField(
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
                Button(onClick = { scope.launch {
                    // Insert habit
                    var num = habitCompletionSteps.text.toInt()
                    habitsDao.insertAll(Habits(0, habitName.text, habitDescription.text, 0, "Daily", completionSteps = num))
                    // Reset form
                    habitName = TextFieldValue("")
                    habitDescription = TextFieldValue("")
                    habitCompletionSteps = TextFieldValue("1")
                    sheetState.hide()
                    onHabitChange()

                }  },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Green)
                ) {
                    Text("Add")
                }
            }
        }
    }

}


