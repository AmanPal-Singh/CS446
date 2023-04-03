package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun Profile() {
    var name by remember {
        mutableStateOf(TextFieldValue("John Smi"))
    }

    var year by remember {
        mutableStateOf(TextFieldValue("2024"))
    }

    var editingEnabled by remember {
        mutableStateOf(false)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        UserField(value = name, enabled = editingEnabled, onChange = { new -> name = new})
        UserField(value = year, enabled = editingEnabled, onChange = { new -> year = new})
        EditButtons(
            editingEnabled = editingEnabled,
            enableEditing = { editingEnabled = true},
            disableEditing = { editingEnabled = false}
        )
    }
}

@Composable
fun UserField(value: TextFieldValue, enabled: Boolean, onChange: (new: TextFieldValue) -> Unit ) {
    OutlinedTextField(
        value = value,
        label = { Text(text = "Name") },
        enabled = enabled,
        onValueChange = {
            onChange(it)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
            backgroundColor = Color.Transparent,
            disabledBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
            disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
        )

    )
}

@Composable
fun EditButtons(editingEnabled: Boolean, enableEditing: () -> Unit, disableEditing: () -> Unit) {
    Row() {
        if (!editingEnabled) {
            Button(onClick = {  enableEditing() }) {
                Text("Edit Profile")
            }
        } else {
            Row () {
                Button(onClick = {
                    // TODO: revert to previous state
                    disableEditing()
                }) {
                    Text("Cancel")
                }
                Button(onClick = {
                    // TODO: save new
                    disableEditing()
                }) {
                    Text("Save")
                }
            }
        }

    }
}