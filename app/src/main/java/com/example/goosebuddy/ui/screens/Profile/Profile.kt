package com.example.goosebuddy.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.R
import com.example.goosebuddy.ui.screens.Onboarding.OnboardingViewModel
import com.example.goosebuddy.ui.screens.Profile.ProfileViewModel
import com.example.goosebuddy.ui.shared.components.*
import com.example.goosebuddy.ui.theme.*

@Composable
fun Profile(db: AppDatabase) {
    val viewModel by remember {
        mutableStateOf(ProfileViewModel(db))
    }

    val userData = viewModel.getUserData()

    var name by remember { mutableStateOf(TextFieldValue(userData.name)) }

    var year by remember { mutableStateOf(TextFieldValue("${userData.year}")) }

    var wat by remember { mutableStateOf(TextFieldValue("${userData.wat}")) }

    val editingEnabled by viewModel.editingEnabled.observeAsState(false)

    val checkBoxStates by remember { mutableStateOf( mutableMapOf(
        "roommate" to mutableStateOf(userData.hasRoommates),
        "res" to mutableStateOf(userData.onStudentRes),
        "alone" to mutableStateOf(userData.firstTimeAlone)
    ))}

    val checkBoxFns by remember { mutableStateOf(mapOf<String, (Boolean) -> Unit>(
        "roommate" to { checkBoxStates["roommate"]!!.value = it},
        "res" to { checkBoxStates["res"]!!.value = it},
        "alone" to { checkBoxStates["alone"]!!.value = it},
    ))}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGrey)
        ,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        HeartAccessory(WavingGoose()).decorate()

        Spacer(modifier = Modifier.size(30.dp))
        UserField(name = "Name", value = name, enabled = editingEnabled, onChange = { new -> name = new })
        UserField(name= "Year", value = year, enabled = editingEnabled, onChange = { new -> year = new})
        UserField(name = "Watcard ID", value = wat, enabled = editingEnabled, onChange = { new -> wat = new })
        CheckboxFields(checkBoxFns = checkBoxFns, state = checkBoxStates, enabled = editingEnabled)
        EditButtons(
            editingEnabled = editingEnabled,
            enableEditing = { viewModel.setEditingEnabled(true)},
            disableEditing = { viewModel.setEditingEnabled(false)},
            revertChanges = {
                name = TextFieldValue(userData.name)
                year = TextFieldValue(userData.year.toString())
                wat = TextFieldValue(userData.wat.toString())
            },
            updateData = {
                userData.name = name.text
                userData.year = year.text.toInt()
                userData.wat = wat.text.toInt()
                userData.hasRoommates = checkBoxStates["roommate"]?.value == true
                userData.firstTimeAlone = checkBoxStates["alone"]?.value == true
                userData.onStudentRes = checkBoxStates["res"]?.value == true
                viewModel.updateUserData(userData)
                Log.i("profile", "user data updated to ${userData}")
                Log.i("profile.userDao", "userDao: ${viewModel.getUserData()}")
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CheckboxFields(checkBoxFns: Map<String, (Boolean) -> Unit>, state: MutableMap<String, MutableState<Boolean>>, enabled: Boolean) {
    Column(
        modifier = Modifier
    ) {
        Spacer(Modifier.height(10.dp))
        Text(
            "Living Conditions",
            fontSize = 12.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            checkBoxFns.keys.forEach {
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = it
                )
                Checkbox(
                    checked = state[it]?.value ?: false,
                    onCheckedChange = { checked_ ->
                        state[it]!!.value = checked_
                        checkBoxFns[it]!!(checked_)
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
            Button(
                onClick = {  enableEditing() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Green)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pencil),
                    tint = Black,
                    contentDescription = "Edit"
                )
                Text("Edit Profile")
            }
        } else {
            Row () {
                OutlinedButton(
                    onClick = { revertChanges()
                        disableEditing() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Grey),
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.size(20.dp))
                Button(
                    onClick = {  // TODO: save new
                        updateData()
                        disableEditing() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Beige)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Save",
                        tint = Black,
                        modifier = Modifier
                            .height(20.dp)
                    )
                    Text("Save")
                }
            }
        }
    }
}