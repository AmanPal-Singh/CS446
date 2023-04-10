package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.models.calendar.CalendarItem
import com.example.goosebuddy.models.calendar.CalendarSeries
import com.example.goosebuddy.models.calendar.getScheduleInfo
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

class CalendarViewModel(
    val calendarState: CalendarState<DynamicSelectionState>,
    val navController: NavController,
    val db: AppDatabase,
    val scrollState: ScrollState
) : ViewModel() {
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