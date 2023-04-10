package com.example.goosebuddy.ui.screens.Calendar

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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