package com.example.goosebuddy.ui.screens


import android.content.Context
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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.room.RoomDatabase
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.ui.theme.*


class Habit(
    // Represents a habit
    var title: String,
    var description: String,
    var completed: Int,
    var schedule: String,
)

val mockHabits = arrayOf(
    Habit("Skincare", "skincare yo", 1, "Daily"),
    Habit("Fitness", "fitness yo", 0, "Weekly"),
)

@Composable
fun Habits(navController: NavController, db: AppDatabase) {
    var habitsDao = db.habitsDao()
    habitsDao.insertAll(Habits(13201392, "Skincare", "skincare yo", 1, "Daily"), Habits(19382, "Fitness", "fitness yo yo", 0, "Weekly"))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Beige)
            .fillMaxHeight()
    ) {
        Box {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                habitsDao.getAll().forEach { item ->
                    HabitBlock(item = item, navController = navController)
                }
            }
            Button(
                onClick = { navController.navigate("habits/create" )},
                colors = ButtonDefaults.buttonColors(backgroundColor = TransluenceBlack),
                modifier = Modifier.align(Alignment.TopCenter))
            {
                Icon(imageVector = Icons.Default.Add, contentDescription = "emailIcon", tint = White)
                Text(text="Add Habit", color = White)
            }
        }

    }
}

@Composable

fun HabitBlock(item: Habits, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) { Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .fillMaxHeight()
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(item.schedule, color = Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(item.title, color = Black,  fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(item.description,  fontSize = 12.sp, color = Grey)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 16.dp)
            ) {
                Button(onClick = {
                    navController.navigate("habits/${item.id}/edit")
                                 },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Black))  {
                    Text(text="Edit", color = White)
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(backgroundColor = Black)){
                    Text(text="Done", color = White)
                }
            }
        }

    }
}

