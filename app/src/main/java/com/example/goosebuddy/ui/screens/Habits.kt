package com.example.goosebuddy.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.room.RoomDatabase
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Habits(navController: NavController, db: AppDatabase) {
    var habitsDao = db.habitsDao()
    habitsDao.insertAll(Habits(13201392, "Skincare", "skincare yo", 1, "Daily"), Habits(19382, "Fitness", "fitness yo yo", 0, "Weekly"))
    var sheetNewContent: @Composable (() -> Unit)  by remember { mutableStateOf({ Text("NULL") }) }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {
            sheetNewContent()
        },
    ) {
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
                        HabitBlock(item = item, navController = navController, db, scope, sheetState,
                            { new -> sheetNewContent = new })
                    }
                }
                Button(
                    onClick = {
                        sheetNewContent = { AddHabit(
                            scope = scope,
                            sheetState = sheetState,
                            db = db,
                            navController = navController
                        )}
                        scope.launch {
                            sheetState.animateTo(ModalBottomSheetValue.Expanded)

                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = TransluenceBlack),
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "emailIcon",
                        tint = White
                    )
                    Text(text = "Add Habit", color = White)
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitBlock(item: Habits, navController: NavController, db: AppDatabase, scope: CoroutineScope, sheetState: ModalBottomSheetState, composable: (it: @Composable (() -> Unit)) -> Unit ) {
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
                    composable {
                        UpdateHabit(
                            scope = scope,
                            sheetState = sheetState,
                            db = db,
                            navController = navController,
                            habitId = item.id
                        )
                    }
                    scope.launch {
                            sheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
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

