package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class Subroutine(
    var name: String,
    var description: String,
    var completed: Boolean
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Routine(name: String, subroutines: Array<Subroutine>, navController: NavHostController) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            AddSubroutine(scope, sheetState)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(name)
            Button(onClick = {navController.navigate("routines/1/timer") }) {
                Text("Resume")
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                subroutines.forEach { it ->
                    SubroutineCard(name = it.name , description = it.description, completed = it.completed)
                }
            }
            Button(
                onClick = {
                    scope.launch {
                        sheetState.show()
                    }
                }
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
fun SubroutineCard(name: String, description: String, completed: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
            ,
        ) {
            Column() {
                Text(name)
                Spacer(Modifier.height(10.dp))
                Text(description)
            }
            Checkbox(checked = completed, onCheckedChange = {})
        }
    }
}
// TODO: add icon picker: https://github.com/maltaisn/icondialoglib
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSubroutine(scope: CoroutineScope, sheetState: ModalBottomSheetState) {
    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var description by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var durationNumber by remember {
        mutableStateOf(TextFieldValue("60"))
    }

    var expanded by remember { mutableStateOf(false) }
    val durationUnitOptions = listOf("s", "min", "hr")
    var selectedUnit by remember { mutableStateOf(0) }
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { newText ->
                name = newText
            },
            label = { Text(text = "Name") },
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = { newText ->
                description = newText
            },
            label = { Text(text = "Description") },
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = durationNumber,
                onValueChange = { newText ->
                    durationNumber = newText
                },
                label = { Text(text = "Duration") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            TextField(
                value = durationUnitOptions[selectedUnit],
                onValueChange = { },
                enabled = false,
                modifier = Modifier
                    .clickable(onClick = { expanded = true }),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                    backgroundColor = Color.Transparent,
                    disabledBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                    disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
                )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                durationUnitOptions.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedUnit = index
                        expanded = false
                    }) {
                        Text(text = s)
                    }
                }
            }
        }
        Button(onClick = { scope.launch {
            // Reset form
            name = TextFieldValue("")
            description = TextFieldValue("")
            durationNumber = TextFieldValue("60")
            selectedUnit = 0
            sheetState.hide()
        }  }) {
            Text("Add")
        }
    }
}