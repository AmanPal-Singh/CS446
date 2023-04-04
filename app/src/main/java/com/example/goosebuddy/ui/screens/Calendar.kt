package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Database
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.CalendarItem
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.Green
import com.example.goosebuddy.ui.theme.Grey
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.LocalDateTime.Companion.parse

const val calendarImportRoute = "calendar/import"

// NOTE this is not a "mutableStateOf", this is just a map I can write to for testing
//val mockCalendarData: MutableMap<LocalDate, MutableList<CalendarItem>> = mutableMapOf()

data class TermData(
    val termCode: String
)

data class ClassScheduleData(
    val classNumber: String,
    val scheduleData: List<ScheduleData>,
)

data class ScheduleData(
    val scheduleStartDate: String,
    val scheduleEndDate: String,
    val classMeetingStartTime: String,
    val classMeetingEndTime: String,
    val classMeetingWeekPatternCode: String
)

class CalendarViewModel(
    val calendarState: CalendarState<DynamicSelectionState>,
    val navController: NavController,
    val db: AppDatabase
) : ViewModel() {
    private suspend fun getScheduleInfo(subject: String, courseNumber: String, classNumber: String) : ScheduleData {
        // First, get current term code
        val termUrl = "https://openapi.data.uwaterloo.ca/v3/Terms/current"
        val (_, _, termResult) = Fuel.get(termUrl)
            .header("X-API-KEY", "0DFD62ADA40F4B4680AE7EFE4B15CE5A")
            .awaitStringResponseResult()
        // By the time this var is used, should be overridden. Or an exception would have been thrown
        var termData = TermData("")

        termResult.fold(
            { data ->
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter(TermData::class.java)
                val nullableTermData = jsonAdapter.nullSafe().lenient().fromJson(data)
                nullableTermData ?.let {
                    termData = nullableTermData
                } ?: {
                    throw Exception("Deserializing term data resulted in null!")
                }
            },
            { error ->
                throw Exception("Error getting term data from UWaterloo API: ${error.message}")
            }
        )

        val termCode = termData.termCode
        val classScheduleUrl = "https://openapi.data.uwaterloo.ca/v3/ClassSchedules/${termCode}/${subject}/${courseNumber}"
        val (_, _, classScheduleResult) = Fuel.get(classScheduleUrl)
                .header("X-API-KEY", "0DFD62ADA40F4B4680AE7EFE4B15CE5A")
                .awaitStringResponseResult()
        // By the time this var is used, should be overridden. Or an exception would have been thrown
        var classScheduleData: List<ClassScheduleData> = listOf()

        classScheduleResult.fold(
            { data ->
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val listType = Types.newParameterizedType(List::class.java, ClassScheduleData::class.java)
                val jsonAdapter: JsonAdapter<List<ClassScheduleData>> = moshi.adapter(listType)
                val nullableClassScheduleData = jsonAdapter.nullSafe().lenient().fromJson(data)
                nullableClassScheduleData ?.let {
                    classScheduleData = nullableClassScheduleData
                } ?: {
                    throw Exception("Deserializing class schedule data resulted in null!")
                }
            },
            { error -> throw Exception("Error getting class schedule data from UWaterloo API: ${error.message}") }
        )

        // Match the provided class number with the available class offerings
        val nullableFound = classScheduleData.find {it ->
            it.classNumber == classNumber
        }
        nullableFound ?.let {
            return nullableFound.scheduleData[0]
        } ?: run {
            throw Exception("No ${subject} ${courseNumber} class found with class number ${classNumber}")
        }
    }

    fun importSchedule(subject: String, courseNumber: String, classNumber: String) {
        viewModelScope.launch {
            try {
                val scheduleData = getScheduleInfo(subject, courseNumber, classNumber)

                val startDate = parse(scheduleData.scheduleStartDate).date
                val endDate = parse(scheduleData.scheduleEndDate).date
                val startTime = parse(scheduleData.classMeetingStartTime).time
                val endTime = parse(scheduleData.classMeetingEndTime).time
                val daysOfWeek = scheduleData.classMeetingWeekPatternCode

                var weekDate = startDate
                val calendarItemDao = db.CalendarItemDao()
                while (weekDate < endDate) {
                    for ((index, c) in daysOfWeek.withIndex()) {
                        if (c == 'Y') {
                            val newDate = weekDate.plus(index, DateTimeUnit.DAY)

                            val course = "$subject $courseNumber"
                            val newCalendarItem = CalendarItem(0, course, newDate, startTime, endTime, false)
                            calendarItemDao.insertAll(newCalendarItem)
                        }
                    }

                    weekDate = weekDate.plus(7, DateTimeUnit.DAY)
                }
            } catch (e: Exception) {
                println("Failed to import schedule: ${e.message}")
                // Some UI feedback?
            }
        }
    }

    fun onSubmitCalendarImport(subject: String, courseNumber: String, classNumber: String) {
        importSchedule(subject, courseNumber, classNumber)

        // With calendar import happening in background, navigate back to calendar screen
        navController.navigate(BottomNavigationItem.Calendar.screen_route)
    }

    fun persistCheckbox(item: CalendarItem, checked: Boolean) {
        val updatedItem = CalendarItem(
            id = item.id,
            title = item.title,
            date = item.date,
            startTime = item.startTime,
            endTime = item.endTime,
            checked = checked
        )
        val dao = db.CalendarItemDao()
        dao.update(updatedItem)
    }

    fun deleteCalendarItem(item: CalendarItem) {
        val dao = db.CalendarItemDao()
        dao.delete(item)
    }
}

@Composable
fun Calendar(
    cvm: CalendarViewModel
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
        SelectableCalendar(calendarState = cvm.calendarState)
        cvm.calendarState.selectionState.selection.forEach { localDate ->
            val kLocalDate = localDate.toKotlinLocalDate()
            ComposeCalendarBlocksForDate(
                cvm = cvm,
                localDate = kLocalDate
            )
        }
        Button(
            onClick = {
                cvm.navController.navigate(calendarImportRoute)
            }) {
            Text("Add course to schedule")
        }
    }
}

@Composable
private fun ComposeCalendarBlocksForDate(cvm: CalendarViewModel, localDate: LocalDate) {
    val dao = cvm.db.CalendarItemDao()
    val items = dao.getOnDate(localDate)
    items.forEach { item ->
        val checked = mutableStateOf(item.checked)
        CalendarBlock(cvm = cvm, item = item, checked = checked)
    }
}

// We pass in checked as a mutable state so when we check the checkbox we can recompose the card without
// Having to recompose the whole page. It is much smoother this way
@Composable
private fun CalendarBlock(cvm: CalendarViewModel, item: CalendarItem, checked: MutableState<Boolean>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
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
                    .padding(15.dp)
            ) {
                Button(
                    onClick = { println("calendar item edit not implemented yet") }
                ) {
                    Text("Edit")
                }
                Button(
                    onClick = {
                        cvm.deleteCalendarItem(item)
                        // Navigate to Calendar (ourself) to recompose stuff
                        cvm.navController.navigate(BottomNavigationItem.Calendar.screen_route)
                    }
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

