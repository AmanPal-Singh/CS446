package com.example.goosebuddy.ui.screens


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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
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
import com.example.goosebuddy.ui.theme.Yellow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.room.RoomDatabase
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.Routines
import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class WeekdayData(
    var weekday: String,
    var completedCount: Int,
    var totalCount: Int
)

val mockWeekdayData = arrayOf(
    WeekdayData("S", 1, 1),
    WeekdayData("M", 5, 10),
    WeekdayData("T", 3, 10),
    WeekdayData("W", 5, 9),
    WeekdayData("Th", 5, 10),
    WeekdayData("F", 1, 10),
    WeekdayData("S", 0, 10),
)

fun getColour(progress: Float): Color {
    if (progress == 1.0f) {
        return Green
    } else if (progress > 0.25f) {
        return Yellow
    } else {
        return Red
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Routines(navController: NavController, db: AppDatabase) {
    var routinesDao = db.routinesDao()
    routinesDao.insertAll(
        Routines(1, "Skincare", "This is a description", 10, 10),
        Routines(2, "Fitness", "This is a description", 75, 100),
        Routines(3, "Yoga", "This is a description", 0, 10),
        Routines(4, "Cleaning", "This is a description", 5, 10),
        Routines(5, "Study", "This is a description", 25, 100),
        );
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {
            AddRoutineForm(sheetState, scope, navController, db)
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(Grey)
                .fillMaxHeight()
        ) {
            RoutineWeeklyTracker()
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AddRoutineBlock(sheetState, scope)

                routinesDao.getAll().forEach { item ->
                    RoutineBlock(item = item, navController = navController)
                }
            }
        }
    }

}

/** Make padding part of theme */
@Composable
fun RoutineWeeklyTracker() {
    Card(
        shape = RoundedCornerShape(7.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            ) {
                Text("Daily Routines", fontSize = 24.sp)
                Text("Weekly view")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                mockWeekdayData.forEach{ day ->
                    var totalHeight = 160
                    var completedHeight = totalHeight * day.completedCount/day.totalCount
                    var leftoverHeight = totalHeight - completedHeight
                    Column(
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Grey)
                                .height(leftoverHeight.dp)
                                .width(10.dp)
                        )
                        Box(
                            modifier = Modifier
                                .background(Green)
                                .height(completedHeight.dp)
                                .width(10.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(day.weekday)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRoutineBlock(sheetState: ModalBottomSheetState, scope: CoroutineScope) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                onClick = {
                    scope.launch {
                        sheetState.show()
                    }
                }
            )
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Add a routine",
            modifier = Modifier
                .padding(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRoutineForm(
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    navController: NavController,
    db: AppDatabase
) {
    var routinesDao = db.routinesDao()
    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var description by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Column {
        SpeechBubble("Honk! Adding a new routine!")
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
                    value = name,
                    onValueChange = { newText ->
                        name = newText
                    },
                    label = { Text(text = "Name") },
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { newText ->
                        description = newText
                    },
                    label = { Text(text = "Description") },
                )
                Button(onClick = {
                    scope.launch {
                        // Add routine
                        routinesDao.insertAll(Routines(0, name.text, description.text, 0, 0))
                        // Reset form
                        name = TextFieldValue("")
                        description = TextFieldValue("")
                        sheetState.hide()
                        navController.navigate(BottomNavigationItem.DailyRoutines.screen_route)
                    }
                }) {
                    Text("Add")
                }
            }
        }
    }
}


/** TODO: Put in componenets directory? */
@Composable
fun RoutineBlock(item: Routines, navController: NavController) {
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
            val checkedState = remember { mutableStateOf(item.totalSteps == item.completedSteps) }
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    if (it) {
                        item.completedSteps = item.totalSteps
                        mockWeekdayData[0].completedCount += 1
                    } else {
                        item.completedSteps = 0
                        mockWeekdayData[0].completedCount -= 1
                    }
                },
                colors=CheckboxDefaults.colors(
                    checkedColor = Green,
                    uncheckedColor = Grey,
                )
            )
            Column {
                Text(item.title)
                Spacer(Modifier.height(20.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .clip(CircleShape)
                        .height(8.dp),
                    progress = item.completedSteps.toFloat()/item.totalSteps,
                    color = getColour(item.completedSteps.toFloat()/item.totalSteps),
                    backgroundColor = Grey,
                )
            }
            IconButton(onClick = { navController.navigate("routines/${item.id}") }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "More",
                    tint = Grey,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}


