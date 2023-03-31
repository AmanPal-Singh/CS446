package com.example.goosebuddy.ui.screens


import android.content.Context
import androidx.compose.runtime.getValue

import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import androidx.room.RoomDatabase
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.ui.theme.Yellow
import com.example.goosebuddy.models.Habits

@Composable
fun AddHabit(navController: NavController, db: AppDatabase) {
    var habitsDao = db.habitsDao()

    // Form values
    var habitName by remember { mutableStateOf(TextFieldValue("")) }
    var habitDescription by remember { mutableStateOf(TextFieldValue("")) }
    var habitSchedule by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Beige)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = habitName,
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
                onValueChange = {
                    habitName = it
                },
                label = { Text(text = "Habit Name") },
                placeholder = { Text(text = "Enter the habit name") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor=White
                )
            )
            OutlinedTextField(
                value = habitDescription,
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
                onValueChange = {
                    habitDescription = it
                },
                label = { Text(text = "Habit Description") },
                placeholder = { Text(text = "Enter the habit description") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor=White
                )
            )
            Button(onClick = {  }, colors = ButtonDefaults.buttonColors(backgroundColor = Black)){
                Text(text="Add Habit", color = White)
            }
        }

    }
}


