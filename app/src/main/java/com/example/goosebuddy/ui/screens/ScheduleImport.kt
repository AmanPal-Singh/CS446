package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ScheduleImportViewModel : ViewModel() {
    val subject = mutableStateOf("")
    val courseNumber = mutableStateOf("")
    val classNumber = mutableStateOf("")
}

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
            onClick = {
                onSubmit(sivm.subject.value, sivm.courseNumber.value, sivm.classNumber.value)
            }) {
            Text("Import schedule")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleImportGoose(
    sivm: ScheduleImportViewModel,
    onSubmit: (String, String, String) -> Unit,
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
) {
    // TODO: Validation
    SpeechBubble("Honk! Adding a course...")
    Goose(200.dp, 8f)
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        SubjectField(sivm = sivm)
        CourseNumberField(sivm = sivm)
        ClassNumberField(sivm = sivm)
        Button(
            onClick = { scope.launch {
                sheetState.hide()
                onSubmit(sivm.subject.value, sivm.courseNumber.value, sivm.classNumber.value)
            }
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
        label = { Text("Subject") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CourseNumberField(sivm: ScheduleImportViewModel) {
    TextField(
        value = sivm.courseNumber.value,
        onValueChange = { sivm.courseNumber.value = it },
        label = { Text("Course Number") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ClassNumberField(sivm: ScheduleImportViewModel) {
    TextField(
        value = sivm.classNumber.value,
        onValueChange = { sivm.classNumber.value = it },
        label = { Text("Class Number") },
        modifier = Modifier.fillMaxWidth()
    )
}

