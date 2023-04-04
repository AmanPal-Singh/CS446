package com.example.goosebuddy.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.R
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.ui.shared.components.DeleteButton
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Habits(navController: NavController, db: AppDatabase) {
    var habitsDao = db.habitsDao()

    var sheetNewContent: @Composable (() -> Unit)  by remember { mutableStateOf({ Text("NULL") }) }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val editingEnabled = remember { mutableStateOf(false) }
    val currentOrder = remember { mutableStateOf(habitsDao.getAll().map { h -> h.id}) }
    val orderState = rememberReorderableLazyListState(onMove = { from, to ->
            currentOrder.value = currentOrder.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
    )

    if (sheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                navController.navigate(BottomNavigationItem.Habits.screen_route)
            }
        }
    }
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
                .background(LightGrey)
                .fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .height(intrinsicSize = IntrinsicSize.Max)
            ) {
                if (editingEnabled.value) {
                    OutlinedButton(
                        onClick = { editingEnabled.value = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Grey),
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = { editingEnabled.value = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Beige)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = "Save",
                            tint = Black,
                            modifier = Modifier
                                .height(20.dp)
                        )
                        Text("Save")
                    }
                } else {
                    Button(
                        onClick = {
                            // toggle the editing enabled state
                            editingEnabled.value = true
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Beige),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pencil),
                            contentDescription = "Edit Habits",
                            tint = Black
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {
                            sheetNewContent = {
                                AddHabit(
                                    scope = scope,
                                    sheetState = sheetState,
                                    db = db,
                                    navController = navController
                                )
                            }
                            scope.launch {
                                sheetState.show()
                                //sheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Grey),
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "plusIcon",
                            tint = Black
                        )
                        Text(text = "Add Habit", color = Black)
                    }
                }
            }
            LazyColumn(
                state = orderState.listState,
                modifier = Modifier
                    .reorderable(orderState)
                    .detectReorderAfterLongPress(orderState),
                // content padding
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 16.dp
                ),
            ) {
                items(currentOrder.value, { it }) { item ->
                    ReorderableItem(orderState, key = item,) { isDragging ->
                        val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                        val habit = habitsDao.getAll().find { h -> h.id == item }
                        if (habit != null) {
                            HabitBlock(item = habit,
                                navController = navController,
                                db,
                                scope,
                                sheetState,
                                { new -> sheetNewContent = new },
                                Modifier.shadow(elevation.value),
                                editingEnabled
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitBlock(item: Habits, navController: NavController, db: AppDatabase, scope: CoroutineScope, sheetState: ModalBottomSheetState, composable: (it: @Composable (() -> Unit)) -> Unit , modifier: Modifier, editingEnabled: MutableState<Boolean>) {
    var habitsDao = db.habitsDao()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(item.title, color = Black, fontSize = 24.sp)
                    Text(item.description, fontSize = 12.sp, color = DarkGray)
                    val isVisible = if (item.streak != 0) 1f else 0f
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(isVisible)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fire),
                            contentDescription = "Streak Fire",
                            tint = Color.Unspecified
                        )
                        Text("${item.streak}")
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                if (!editingEnabled.value) {
                    Button(
                        enabled = item.currentlyCompletedSteps != 0,
                        onClick = {
                            item.currentlyCompletedSteps -= 1
                            habitsDao.update(item)
                            navController.navigate(BottomNavigationItem.Habits.screen_route)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Beige),
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Transparent, CircleShape)
                    ) {
                        Text(text = "-")
                    }
                }
                CircularProgressIndicator(
                    progress = (item.currentlyCompletedSteps.toFloat() / item.completionSteps.toFloat()),
                    color = LightBlue,
                    backgroundColor = Grey,
                    strokeWidth = 5.dp,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                )

                if (!editingEnabled.value) {
                    Button(
                        onClick = {
                            item.currentlyCompletedSteps += 1
                            if (item.currentlyCompletedSteps == item.completionSteps) {
                                item.streak += 1
                            }
                            habitsDao.update(item)
                            navController.navigate(BottomNavigationItem.Habits.screen_route)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Transparent, CircleShape)
                    ) {
                        Text(text = "+")
                    }
                }
            }

            Text("${item.currentlyCompletedSteps} / ${item.completionSteps}", color = LightBlue)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 16.dp)
            ) {
                if (editingEnabled.value) {
                    Column(
                        modifier = Modifier
                            .padding(2.dp),
                        horizontalAlignment = Alignment.End,
                    ) {
                        DeleteButton(
                            onDelete = {
                                habitsDao.delete(item)
                                navController.navigate(BottomNavigationItem.Habits.screen_route)
                            }
                        )
                    }
                }
                Button(
                    onClick = {
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
                            sheetState.show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Black)
                ) {
                    Text(text = "Edit", color = White)
                }
            }
        }
    }
}

