package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.background
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
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.ui.theme.Green
import com.example.goosebuddy.ui.theme.White
import androidx.lifecycle.viewModelScope
import com.example.goosebuddy.models.CalendarSeries
import com.example.goosebuddy.ui.shared.components.DeleteButton
import com.example.goosebuddy.ui.shared.components.textFieldStyleBlue
import com.example.goosebuddy.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction3

class ScheduleImportViewModel : ViewModel() {
    val subject = mutableStateOf("")
    val courseNumber = mutableStateOf("")
    val classNumber = mutableStateOf("")

    fun clear() {
        subject.value = ""
        courseNumber.value = ""
        classNumber.value = ""
    }
}

val IMPORT_SCHEDULE_ROUTE = "calendar/courseSchedule"

@Composable
fun ScheduleImport(
    sivm: ScheduleImportViewModel,
    onSubmit: (String, String, String) -> Unit,
) {
    // TODO: Validation
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        SubjectField(sivm = sivm)
        CourseNumberField(sivm = sivm)
        ClassNumberField(sivm = sivm)
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Green),
            onClick = {
                onSubmit(sivm.subject.value, sivm.courseNumber.value, sivm.classNumber.value)
            },
        ) {
            Text("Import schedule", color = Black)
        }
    }
}

@Composable
fun ScheduleImportPage(
    sivm: ScheduleImportViewModel,
    onSubmit: KSuspendFunction3<String, String, String, Unit>,
    cvm: CalendarViewModel,
) {
    val calendarSeriesDao = cvm.db.CalendarSeriesDao()
    val courseList = remember { mutableStateOf(calendarSeriesDao.getAll()) }

    // TODO: Validation
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGrey)
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SubjectField(sivm = sivm)
        CourseNumberField(sivm = sivm)
        ClassNumberField(sivm = sivm)
        Button(
            onClick = {
                cvm.viewModelScope.launch {
                    onSubmit(sivm.subject.value, sivm.courseNumber.value, sivm.classNumber.value)
                    sivm.clear()
                    // Update mutable state course list so view updates
                    courseList.value = calendarSeriesDao.getAll()
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Beige)
        ) {
            Text("Import schedule", color = Black)
        }
        Divider(
            modifier = Modifier
                .padding(0.dp, 15.dp)
        )

        CourseList(cvm, courseList)
    }
}

@Composable
private fun CourseList(cvm: CalendarViewModel, courseList: MutableState<List<CalendarSeries>>) {
    Text("Course List")
    val calendarSeriesDao = cvm.db.CalendarSeriesDao()
    val calendarItemDao = cvm.db.CalendarItemDao()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for ( (index, course) in courseList.value.withIndex()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Text(text = course.description)
                    DeleteButton(
                        onDelete = {
                            // Delete all calendar items with this series
                            // Must do this first to satisfy foreign key constraint
                            calendarItemDao.deleteInSeries(course.id)

                            // Delete calendar series
                            calendarSeriesDao.delete(course)

                            // Delete from mutableState so UI updates
                            val newList = courseList.value.toMutableList()
                            newList.removeAt(index)
                            courseList.value = newList.toList()
                        },
                    )
                }
            }

        }
    }
}

@Composable
private fun SubjectField(sivm: ScheduleImportViewModel) {
    OutlinedTextField(
        value = sivm.subject.value,
        onValueChange = { sivm.subject.value = it },
        label = { Text("Subject") },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun CourseNumberField(sivm: ScheduleImportViewModel) {
    OutlinedTextField(
        value = sivm.courseNumber.value,
        onValueChange = { sivm.courseNumber.value = it },
        label = { Text("Course Number") },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ClassNumberField(sivm: ScheduleImportViewModel) {
    OutlinedTextField(
        value = sivm.classNumber.value,
        onValueChange = { sivm.classNumber.value = it },
        label = { Text("Class Number") },
        modifier = Modifier.fillMaxWidth(),
    )
}

