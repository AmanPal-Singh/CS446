package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ScheduleImportViewModel : ViewModel() {
    val subject = mutableStateOf("")
    val courseNumber = mutableStateOf("")
    val classNumber = mutableStateOf("")
}

@Composable
fun ScheduleImport(sivm: ScheduleImportViewModel, onSubmit: (String, String, String) -> Unit) {
    // TODO: Validation
    Column() {
        SubjectField(sivm = sivm)
        CourseNumberField(sivm = sivm)
        ClassNumberField(sivm = sivm)
        Button(
            onClick = {
                onSubmit(sivm.subject.value, sivm.courseNumber.value, sivm.classNumber.value)
        }) {
            Text("Import schedule")
        }
    }
}

@Composable
private fun SubjectField(sivm: ScheduleImportViewModel) {
    TextField(
        value = sivm.subject.value,
        onValueChange = { sivm.subject.value = it },
        label = { Text("Subject") }
    )
}

@Composable
private fun CourseNumberField(sivm: ScheduleImportViewModel) {
    TextField(
        value = sivm.courseNumber.value,
        onValueChange = { sivm.courseNumber.value = it },
        label = { Text("Course Number") }
    )
}

@Composable
private fun ClassNumberField(sivm: ScheduleImportViewModel) {
    TextField(
        value = sivm.classNumber.value,
        onValueChange = { sivm.classNumber.value = it },
        label = { Text("Class Number") }
    )
}

