package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


class Subroutine(
    var name: String,
    var description: String,
    var completed: Boolean
)



@Composable
fun Routine(name: String, subroutines: Array<Subroutine>, navController: NavHostController) {
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
        Button(onClick = { }) {
            Text("Add")
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