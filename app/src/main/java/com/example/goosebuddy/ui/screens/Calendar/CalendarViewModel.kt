package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.CalendarItem
import com.example.goosebuddy.models.CalendarSeries
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

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
    val db: AppDatabase,
    val scrollState: ScrollState
) : ViewModel() {
    val API_KEY_HEADER = "X-API-KEY"
    val API_KEY = "0DFD62ADA40F4B4680AE7EFE4B15CE5A" // TODO: this is bad extract to env var later
    val TERM_URL = "https://openapi.data.uwaterloo.ca/v3/Terms/current"

    private suspend fun getScheduleInfo(subject: String, courseNumber: String, classNumber: String) : ScheduleData {
        // First, get current term code

        val (_, _, termResult) = Fuel.get(TERM_URL)
            .header(API_KEY_HEADER, API_KEY)
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
            .header(API_KEY_HEADER, API_KEY)
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

    // I made this suspend instead of wrapping in a launch because sometimes I need to wait for
    // this db operation to finish, then do something.
    suspend fun importSchedule(subject: String, courseNumber: String, classNumber: String) {
            try {
                // First, verify course exists. Below should exception if API call fails
                val scheduleData = getScheduleInfo(subject, courseNumber, classNumber)

                // Then, add course as CalendarSeries
                val calendarSeriesDao = db.CalendarSeriesDao()
                val description = "${subject} ${courseNumber} - #${classNumber}"
                val seriesId = calendarSeriesDao.insert(CalendarSeries(0, description)).toInt()

                // Then, add each calendar item for the course
                val startDate = LocalDateTime.parse(scheduleData.scheduleStartDate).date
                val endDate = LocalDateTime.parse(scheduleData.scheduleEndDate).date
                val startTime = LocalDateTime.parse(scheduleData.classMeetingStartTime).time
                val endTime = LocalDateTime.parse(scheduleData.classMeetingEndTime).time
                val daysOfWeek = scheduleData.classMeetingWeekPatternCode

                var weekDate = startDate
                val calendarItemDao = db.CalendarItemDao()
                while (weekDate < endDate) {
                    for ((index, c) in daysOfWeek.withIndex()) {
                        if (c == 'Y') {
                            val newDate = weekDate.plus(index, DateTimeUnit.DAY)

                            val course = "$subject $courseNumber"
                            val newCalendarItem = CalendarItem(0, seriesId, course, newDate, startTime, endTime, false)
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

//    fun onSubmitCalendarImport(subject: String, courseNumber: String, classNumber: String) {
//        importSchedule(subject, courseNumber, classNumber)
//    }

    fun persistCheckbox(item: CalendarItem, checked: Boolean) {
        val updatedItem = CalendarItem(
            id = item.id,
            seriesId = item.seriesId,
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