package com.example.goosebuddy.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.R
import com.example.goosebuddy.models.BottomNavigationItem
import com.example.goosebuddy.models.RoutineWithSubroutine
import com.example.goosebuddy.ui.shared.components.DeleteButton

import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

import com.example.goosebuddy.models.Subroutines
import com.example.goosebuddy.ui.shared.components.DefaultGoose


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Routine(id: Int, navController: NavHostController, db: AppDatabase) {
    var subroutinesDao = db.subroutinesDao()
    var routinesDao = db.routinesDao()
    var routine = routinesDao.get(id)
    var subroutines = routine.subroutines;

    val editingEnabled = remember { mutableStateOf(false) }
    val currentOrder = remember { mutableStateOf(subroutines.map { s -> s.subId }) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        if (editingEnabled.value) {
            currentOrder.value = currentOrder.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
    })

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {
            AddSubroutine(routine, scope, sheetState, navController, db)
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(LightGrey)
        ) {
            Text(routine.routines.title, fontSize = 24.sp)
            Text(routine.routines.description)
            TopActionButtons(editingEnabled = editingEnabled, navController = navController, db = db, routine = routine)
            LazyColumn(
                state = state.listState,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .reorderable(state)
                    .detectReorderAfterLongPress(state)
            ) {
                items(currentOrder.value) { item->
                    ReorderableItem(state, key = item) { isDragging ->
                        val subroutine = subroutines.find { s -> s.subId == item }
                        val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                        if (subroutine != null) {
                            SubroutineCard(
                                id = routine.routines.id,
                                subroutine = subroutine,
                                editingEnabled = editingEnabled,
                                elevation = elevation.value,
                                navController = navController,
                                db = db
                            )
                        }
                    }
                }
            }
            ActionButtons(editingEnabled = editingEnabled, sheetState, scope)
        }
    }
}

@Composable
fun TopActionButtons(
    editingEnabled: MutableState<Boolean>,
    navController: NavHostController,
    db: AppDatabase,
    routine: RoutineWithSubroutine
) {
    var routinesDao = db.routinesDao()
    val id = routine.routines.id
    if (editingEnabled.value) {
        OutlinedButton(
            onClick = {
                // routinesDao.deleteRoutine(routine.routines.id)
                navController.navigate(BottomNavigationItem.DailyRoutines.screen_route)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Red,
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            modifier = Modifier
                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                .height(40.dp)
        ) {
            Text("Delete routine", fontSize = 16.sp)
            Icon(
                painter =  painterResource(id = R.drawable.delete),
                contentDescription = "",
                tint = Color.DarkGray
            )
        }
    } else {
        OutlinedButton(
            onClick = { navController.navigate("routines/1/timer")  },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Green,
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            modifier = Modifier
                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                .height(40.dp)
        ) {
            Text("Start", fontSize = 16.sp)
            Icon(
                painter =  painterResource(id = R.drawable.play_arrow),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionButtons(
    editingEnabled: MutableState<Boolean>,
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        if (editingEnabled.value) {
            Button(
                onClick = { editingEnabled.value = false },
                colors = ButtonDefaults.buttonColors(backgroundColor = Grey),
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.size(5.dp))
            Button(
                onClick = { editingEnabled.value = false },
                colors = ButtonDefaults.buttonColors(backgroundColor = Beige),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = "save icon",
                    tint = Black,
                    modifier = Modifier
                        .height(20.dp)
                )
                Text("Save")
            }
        } else {
            Button(
                onClick = { editingEnabled.value = true },
                colors = ButtonDefaults.buttonColors(backgroundColor = Beige),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pencil),
                        contentDescription = "Edit Routine",
                        tint = Black
                    )
                }
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Button(
                onClick = {
                    scope.launch {
                        sheetState.show()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Grey),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "plusIcon",
                    tint = Black
                )
                Text(text = "Add step to routine", color = Black)
            }
        }
    }
}

@Composable
fun SubroutineCard(
    id: Int,
    subroutine: Subroutines,
    editingEnabled: MutableState<Boolean>,
    elevation: Dp,
    navController: NavController,
    db: AppDatabase
) {
    val subroutinesDao = db.subroutinesDao()
    val textStyle = if (subroutine.completed) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(elevation)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Column() {
                Text(
                    subroutine.title,
                    style = textStyle
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    subroutine.description,
                    style = textStyle
                )
            }
            if (!editingEnabled.value) {
                Text(
                    subroutine.duration.toString(),
                )
            } else {
                DeleteButton(onDelete = {
                    subroutinesDao.delete(subroutine)
                    navController.navigate("routines/${id}")
                })
            }

        }
    }
}
// TODO: add icon picker: https://github.com/maltaisn/icondialoglib
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSubroutine(
    routine: RoutineWithSubroutine,
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    navController: NavController,
    db: AppDatabase
) {
    var subroutinesDao = db.subroutinesDao()

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
    Column {
        SpeechBubble("Honk! Adding a subroutine...")
        DefaultGoose().decorate()
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
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
                Row{
                    Button(onClick = { scope.launch {
                        // Add subroutine
                        subroutinesDao.insertAll(Subroutines(0, routine.routines.id, name.text, description.text, false, durationNumber.text.toInt()))
                        // Reset form
                        name = TextFieldValue("")
                        description = TextFieldValue("")
                        durationNumber = TextFieldValue("60")
                        selectedUnit = 0
                        sheetState.hide()
                        navController.navigate("routines/${routine.routines.id}")

                    }  },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Green)
                    ) {
                        Text("Add")
                    }
                }

            }
        }
    }

}