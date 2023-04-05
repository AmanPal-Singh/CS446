package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goosebuddy.models.CalendarItem
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.*
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Calendar(
    cvm: CalendarViewModel
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val sheetContent: MutableState<@Composable () -> Unit> = remember { mutableStateOf({Text("NULL")}) }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {
            sheetContent.value()
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(cvm.scrollState)
                .background(LightGrey)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text("Calendar", fontSize = 40.sp)
            }
            Button(
                onClick = {
                    sheetContent.value = {
                        val sivm = ScheduleImportViewModel()
                        ScheduleImportGoose(
                            sivm = sivm,
                            onSubmit = cvm::onSubmitCalendarImport,
                            sheetState = sheetState,
                            scope = coroutineScope,
                        )
                    }
                    coroutineScope.launch {
                        sheetState.show()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Beige)
            ) {
                Text("Add course to schedule", color = Black)
            }
            SelectableCalendar(
                calendarState = cvm.calendarState,
            )
            Divider(
                modifier = Modifier
                    .padding(0.dp, 15.dp)
            )
            cvm.calendarState.selectionState.selection.forEach { localDate ->
                val kLocalDate = localDate.toKotlinLocalDate()
                ComposeCalendarBlocksForDate(
                    cvm = cvm,
                    localDate = kLocalDate,
                    sheetContent,
                    coroutineScope,
                    sheetState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ComposeCalendarBlocksForDate(
    cvm: CalendarViewModel,
    localDate: LocalDate,
    sheetContent: MutableState<@Composable () -> Unit>,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
) {
    val dao = cvm.db.CalendarItemDao()
    val items = dao.getOnDate(localDate)
    
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Text(
                text = "${localDate.month} ${localDate.dayOfMonth}",
                textAlign = TextAlign.Left,
            )
            Button(
                onClick = {
                    sheetContent.value = {
                        val addTaskCalendarItem = CalendarItem(
                            id = 0,
                            title = "",
                            date = localDate,
                            startTime = LocalTime(9, 0, 0),
                            endTime = LocalTime(10, 0, 0),
                            checked = false
                        )
                        val civm = CalendarItemViewModel(addTaskCalendarItem)
                        CalendarItem(
                            civm = civm,
                            mode = "add",
                            cvm = cvm,
                            scope = coroutineScope,
                            sheetState = sheetState,
                        )
                    }
                    coroutineScope.launch {
                        sheetState.show()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Beige)
            ) {
                Text("Add task")
            }
        }

        items.forEach { item ->
            val checked = mutableStateOf(item.checked)
            CalendarBlock(cvm = cvm, item = item, checked = checked, sheetContent, coroutineScope, sheetState)
        }
    }
    
}

// We pass in checked as a mutable state so when we check the checkbox we can recompose the card without
// Having to recompose the whole page. It is much smoother this way
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CalendarBlock(
    cvm: CalendarViewModel,
    item: CalendarItem,
    checked: MutableState<Boolean>,
    sheetContent: MutableState<@Composable () -> Unit>,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Column {
                    Text(item.title)
                    Text(item.startTime.toString() + " - " + item.endTime.toString())
                }
                Column {
                    Checkbox(
                        checked = checked.value,
                        onCheckedChange = {
                            cvm.persistCheckbox(item, it)
                            checked.value = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Green,
                            uncheckedColor = Grey,
                        )
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 0.dp, 15.dp, 15.dp)
            ) {
                Button(
                    onClick = {
                        sheetContent.value = {
                            val civm = CalendarItemViewModel(item)
                            CalendarItem(
                                civm = civm,
                                mode = "edit",
                                cvm = cvm,
                                scope = coroutineScope,
                                sheetState = sheetState,
                            )
                        }
                        coroutineScope.launch {
                            sheetState.show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Grey)
                ) {
                    Text("Edit", color = Black)
                }
                Button(
                    onClick = {
                        cvm.deleteCalendarItem(item)
                        // Navigate to Calendar (ourself) to recompose stuff
                        cvm.navController.navigate(BottomNavigationItem.Calendar.screen_route)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Grey)
                ) {
                    Text("Delete", color = Black)
                }
            }
        }
    }
}

