package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goosebuddy.ui.theme.Green
import com.example.goosebuddy.ui.theme.Grey
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.time.LocalDate

class CalendarItem(
    var title: String,
    var time: String, // TODO: Maybe change this to Time type or a begin end
    var checked: MutableState<Boolean> = mutableStateOf(false)
)

val mockCalendarData = mapOf(
    LocalDate.of(2023, 3, 12) to listOf(
        CalendarItem("Volleyball game", "4:00 PM"),
        CalendarItem("Develop an app", "7:00 PM"),
        CalendarItem("Soccer game", "10:00 PM")
    ),
    LocalDate.of(2023, 3, 13) to listOf(
        CalendarItem("CS 446 Demo", "12:00 PM"),
        CalendarItem("Gym", "2:00 PM"),
        CalendarItem("Eat Lazeez", "6:00 PM"),
        CalendarItem("Bathroom break", "6:05 PM")
    ),
    LocalDate.of(2023, 3, 14) to listOf(
        CalendarItem("Math test", "10:00 AM"),
        CalendarItem("Shower", "7:00 PM")
    )
)

@Composable
fun Calendar(
    calendarState: CalendarState<DynamicSelectionState>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text("Calendar", fontSize = 24.sp)
        }
        SelectableCalendar(calendarState = calendarState)
        calendarState.selectionState.selection.forEach{localDate -> ComposeCalendarBlocksForDate(
            localDate = localDate
        ) }
    }
}

@Composable
fun ComposeCalendarBlocksForDate(localDate: LocalDate) {
    mockCalendarData[localDate]?.forEach { item -> CalendarBlock(item = item) }
}

@Composable
fun CalendarBlock(item: CalendarItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Column {
                Text(item.title)
                Text(item.time)
            }
            Column {
                Checkbox(
                    checked = item.checked.value,
                    onCheckedChange = {
                        item.checked.value = it
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Green,
                        uncheckedColor = Grey,
                    )
                )
            }

        }
    }
}

@Preview
@Composable
fun CalendarBlockPreview() {
    CalendarBlock(CalendarItem("Poop", "12:00 AM"))
}
