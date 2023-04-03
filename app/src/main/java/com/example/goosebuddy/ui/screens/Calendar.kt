package com.example.goosebuddy.ui.screens

import android.annotation.SuppressLint
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.theme.Green
import com.example.goosebuddy.ui.theme.Grey
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.awaitResponse
import com.github.kittinunf.fuel.core.awaitResponseResult
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.*
import kotlinx.datetime.LocalDateTime.Companion.parse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.HttpURLConnection

class CalendarItem(
    var title: String,
    var time: String, // TODO: Maybe change this to Time type or a begin & end
    var checked: MutableState<Boolean> = mutableStateOf(false)
)

// NOTE this is not a "mutableStateOf", this is just a map I can write to
val mockCalendarData: MutableMap<LocalDate, MutableList<CalendarItem>> = mutableMapOf()

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
) : ViewModel() {
    // Test Waterloo API
    // Need async https://developer.android.com/kotlin/coroutines
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
                while (weekDate < endDate) {
                    for ((index, c) in daysOfWeek.withIndex()) {
                        if (c == 'Y') {
                            val newDate = weekDate.plus(index, DateTimeUnit.DAY)
                            if (mockCalendarData.containsKey(newDate)) {
                                mockCalendarData[newDate]?.add(
                                    CalendarItem("$subject $courseNumber", "${startTime} - ${endTime}")
                                )
                            } else {
                                mockCalendarData[newDate] = mutableListOf(
                                    CalendarItem("$subject $courseNumber", "${startTime} - ${endTime}")
                                )
                            }
                        }
                    }

                    weekDate = weekDate.plus(7, DateTimeUnit.DAY)
                }
            } catch (e: Exception) {
                println("Failed to import schedule: ${e.message}")
                // Some UI feedback?
            }
        }

        // With calendar import happening in background, navigate back to calendar screen
        navController.navigate(BottomNavigationItem.Calendar.screen_route)
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
                localDate = kLocalDate
            )
        }
        Button(
            onClick = {
                cvm.navController.navigate("scheduleImportForm")
            }) {
            Text("Add course to schedule")
        }
    }
}

@Composable
private fun ComposeCalendarBlocksForDate(localDate: LocalDate) {
    mockCalendarData[localDate]?.forEach { item -> CalendarBlock(item = item) }
}

@Composable
private fun CalendarBlock(item: CalendarItem) {
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

