package com.example.goosebuddy.ui.screens


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
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.goosebuddy.ui.theme.Yellow


class Habit(
    // Represents a habit
    var title: String,
    var description: String,
    var completed: Int,
)

val mockHabits = arrayOf(
    Habit("Skincare", "skincare yo", 1),
    Habit("Fitness", "fitness yo", 0),
)

@Composable
fun Habits(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Grey)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            mockHabits.forEach { item ->
                HabitBlock(item = item, navController = navController)
            }
        }
    }
}

@Composable

fun HabitBlock(item: Habit, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Column {
                Text(item.title)
                Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(10.dp))
                Text(item.description)
                Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

