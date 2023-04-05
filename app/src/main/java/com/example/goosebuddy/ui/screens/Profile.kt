package com.example.goosebuddy.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.theme.Green

@Composable
fun Profile(db: AppDatabase) {
    val profileDao = db.userdataDao()

    val userData = profileDao.getAll()

    var name by remember { mutableStateOf(TextFieldValue(userData.name)) }

    var year by remember { mutableStateOf(TextFieldValue("${userData.year}")) }

    var wat by remember { mutableStateOf(TextFieldValue("${userData.wat}")) }

    var editingEnabled by remember { mutableStateOf(false) }

    var checkBoxStates by remember { mutableStateOf( mutableMapOf(
        "roommate" to userData.hasRoommates,
        "res" to userData.onStudentRes,
        "alone" to userData.firstTimeAlone
    ))}

    val checkBoxFns by remember { mutableStateOf(mapOf<String, (Boolean) -> Unit>(
        "roommate" to { checkBoxStates["roommates"] = it},
        "res" to { checkBoxStates["res"] = it},
        "alone" to { checkBoxStates["alone"] = it},
    ))}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Goose(size=225.dp, honkSound = true)
        Spacer(modifier = Modifier.size(30.dp))
        UserField(name = "Name", value = name, enabled = editingEnabled, onChange = { new -> name = new })
        UserField(name= "Year", value = year, enabled = editingEnabled, onChange = { new -> year = new})
        UserField(name = "Watcard ID", value = wat, enabled = editingEnabled, onChange = { new -> wat = new })
        CheckboxFields(checkBoxFns = checkBoxFns, state = checkBoxStates, enabled = editingEnabled)
        EditButtons(
            editingEnabled = editingEnabled,
            enableEditing = { editingEnabled = true},
            disableEditing = { editingEnabled = false},
            revertChanges = {
                name = TextFieldValue(userData.name)
                year = TextFieldValue(userData.year.toString())
                wat = TextFieldValue(userData.wat.toString())
            },
            updateData = {
                userData.name = name.text
                userData.year = year.text.toInt()
                userData.wat = wat.text.toInt()
                profileDao.update(userData)
                userData.hasRoommates = checkBoxStates["roommate"] == true
                userData.firstTimeAlone = checkBoxStates["alone"] == true
                userData.onStudentRes = checkBoxStates["res"] == true
                Log.i("profile", "user data updated to ${userData}")
                Log.i("profile.userDao", "userDao: ${profileDao.getAll()}")
            }
        )
    }
}

@Composable
fun CheckboxFields(checkBoxFns: Map<String, (Boolean) -> Unit>, state: MutableMap<String, Boolean>, enabled: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        checkBoxFns.keys.forEach {
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = it
            )
            Checkbox(
                checked = state[it] == true,
                onCheckedChange = { checked_ ->
                    checkBoxFns[it]!!(checked_)
                    state[it] = checked_
                    Log.i("profile", state[it].toString())
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Green
                ),
                enabled = enabled
            )
        }
    }
}

@Composable
fun UserField(name: String, value: TextFieldValue, enabled: Boolean, onChange: (new: TextFieldValue) -> Unit ) {
    OutlinedTextField(
        value = value,
        label = { Text(text = name) },
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
fun EditButtons(editingEnabled: Boolean, revertChanges: () -> Unit, enableEditing: () -> Unit, disableEditing: () -> Unit, updateData: () -> Unit) {
    Row() {
        if (!editingEnabled) {
            Button(onClick = {  enableEditing() }) {
                Text("Edit Profile")
            }
        } else {
            Row () {
                Button(onClick = {
                    revertChanges()
                    disableEditing()
                }) {
                    Text("Cancel")
                }
                Button(onClick = {
                    // TODO: save new
                    updateData()
                    disableEditing()
                }) {
                    Text("Save")
                }
            }
        }

    }
}