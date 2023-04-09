package com.example.goosebuddy.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.models.BottomNavigationItem
import com.example.goosebuddy.models.CalendarItem
import com.example.goosebuddy.ui.shared.components.DefaultGoose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.shared.components.textFieldStyleBlue
import com.example.goosebuddy.ui.theme.Beige
import com.example.goosebuddy.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class CalendarItemViewModel(
    val id: Int,
    val seriesId: Int?,
    title: String,
    date: LocalDate,
    startTime: LocalTime,
    endTime: LocalTime,
    val checked: Boolean
) : ViewModel() {
    val title = mutableStateOf(title)
    val date = mutableStateOf(date)
    val startTime = mutableStateOf(startTime)
    val endTime = mutableStateOf(endTime)
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
        DefaultGoose().decorate()
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
                    colors = textFieldStyleBlue()
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Start time: ", )
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
                            style = TextStyle(
                                color = Color.Blue,
                                fontSize = 16.sp
                            )
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("End time: ")
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
                            style = TextStyle(
                                color = Color.Blue,
                                fontSize = 16.sp
                            )
                        )
                    }
                }

                // Add or edit button
                if (mode == "add") {
                    Button(
                        onClick = {
                            val dao = cvm.db.CalendarItemDao()
                            val newCalendarItem = CalendarItem(
                                id = civm.id,
                                seriesId = civm.seriesId,
                                title = civm.title.value,
                                date = civm.date.value,
                                startTime = civm.startTime.value,
                                endTime = civm.endTime.value,
                                checked = civm.checked,
                            )
                            dao.insertAll(newCalendarItem)

                            scope.launch {
                                // Add
                                sheetState.hide()
                                cvm.navController.navigate(BottomNavigationItem.Calendar.screen_route)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Beige)
                    ) {
                        Text("Add")
                    }
                } else {
                    // Edit
                    Button(onClick = {
                        val dao = cvm.db.CalendarItemDao()
                        val updatedCalendarItem = CalendarItem(
                            id = civm.id,
                            seriesId = civm.seriesId,
                            title = civm.title.value,
                            date = civm.date.value,
                            startTime = civm.startTime.value,
                            endTime = civm.endTime.value,
                            checked = civm.checked,
                        )
                        dao.update(updatedCalendarItem)

                        scope.launch {
                            // Add
                            sheetState.hide()
                            cvm.navController.navigate(BottomNavigationItem.Calendar.screen_route)
                        }
                    },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Beige)
                    ) {
                        Text("Update")
                    }
                }

            }
        }
    }
}


