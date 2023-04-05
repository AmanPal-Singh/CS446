package com.example.goosebuddy.ui.screens

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.goosebuddy.models.CalendarItem
import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

class CalendarItemViewModel(calendarItem: CalendarItem) : ViewModel() {
    val id = calendarItem.id
    val title = mutableStateOf(calendarItem.title)
    val date = mutableStateOf(calendarItem.date)
    val startTime = mutableStateOf(calendarItem.startTime)
    val endTime = mutableStateOf(calendarItem.endTime)
    val checked = calendarItem.checked
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarItem(
    civm: CalendarItemViewModel,
    mode: String,
    cvm: CalendarViewModel,
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState,
) {
    val ctx = LocalContext.current
    Column {
        SpeechBubble("Honk! ${if (mode == "add") "Adding" else "Editing"} a Task...")
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
                    .padding(15.dp)
                    .background(White)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = civm.title.value,
                    onValueChange = { newTitle ->
                        civm.title.value = newTitle
                    },
                    label = { Text("Subject") },
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ClickableText(
                        text = AnnotatedString(civm.startTime.value.toString()),
                        onClick = {
                            val timePicker = TimePickerDialog(
                                ctx,
                                { _, selectedHour, selectedMinute ->
                                    civm.startTime.value =
                                        LocalTime(selectedHour, selectedMinute, 0)
                                },
                                civm.startTime.value?.hour!!, civm.startTime.value?.minute!!, false
                            )
                            timePicker.show()
                        },
                        modifier = Modifier,
                    )
                    ClickableText(
                        text = AnnotatedString(civm.endTime.value.toString()),
                        onClick = {
                            val timePicker = TimePickerDialog(
                                ctx,
                                { _, selectedHour, selectedMinute ->
                                    civm.endTime.value =
                                        LocalTime(selectedHour, selectedMinute, 0)
                                },
                                civm.endTime.value?.hour!!, civm.endTime.value?.minute!!, false
                            )
                            timePicker.show()
                        },
                        modifier = Modifier,
                    )
                }

                // Add or edit button
                if (mode == "add") {
                    Button(onClick = {
                        val dao = cvm.db.CalendarItemDao()
                        val newCalendarItem = CalendarItem(
                            id = civm.id,
                            title = civm.title.value,
                            date = civm.date.value!!,
                            startTime = civm.startTime.value!!,
                            endTime = civm.endTime.value!!,
                            checked = civm.checked,
                        )
                        dao.insertAll(newCalendarItem)

                        scope.launch {
                            // Add
                            sheetState.hide()
                            cvm.navController.navigate(BottomNavigationItem.Calendar.screen_route)
                        }
                    }) {
                        Text("Add")
                    }
                } else {
                    // Edit
                    Button(onClick = {
                        val dao = cvm.db.CalendarItemDao()
                        val updatedCalendarItem = CalendarItem(
                            id = civm.id,
                            title = civm.title.value,
                            date = civm.date.value!!,
                            startTime = civm.startTime.value!!,
                            endTime = civm.endTime.value!!,
                            checked = civm.checked,
                        )
                        dao.update(updatedCalendarItem)

                        scope.launch {
                            // Add
                            sheetState.hide()
                            cvm.navController.navigate(BottomNavigationItem.Calendar.screen_route)
                        }
                    }) {
                        Text("Update")
                    }
                }

            }
        }
    }
}


