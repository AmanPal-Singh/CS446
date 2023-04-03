package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.R
import com.example.goosebuddy.models.Lock
import com.example.goosebuddy.ui.theme.*

@Composable
fun Lock(db: AppDatabase, navController: NavController) {
    val lockDao = db.lockDao()

    GooseBuddyTheme() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            var pin by remember { mutableStateOf("") }
            var isError by rememberSaveable { mutableStateOf(false) }

            if(lockDao.getAll().isEmpty()) {
                Text(
                    "Welcome !\nPlease enter your pin to register it.",
                    textAlign = TextAlign.Center,
                )
            }else{
                Text(
                    "Welcome back!\nPlease enter your pin to proceed.",
                    textAlign = TextAlign.Center,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.goose),
                contentDescription = "Goose.",
                modifier = Modifier
                    .size(200.dp),
                colorFilter = ColorFilter.tint(Yellow)
            )
            TextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("PIN") },
                value = pin,
                isError = isError,
                onValueChange = { newText -> pin = newText.trim() },
                visualTransformation = PasswordVisualTransformation()
            )
            if (isError){
                Text("PIN Invalid")
            }
            Button(
                onClick = {
                    if(lockDao.getAll().isEmpty()){
                        lockDao.insert(Lock(0, pin.toInt()))
                    }else{
                        val valid_pin = lockDao.getAll()
                        if(valid_pin[0].value == pin.toInt()){
                            println("OK")
                            navController.navigate("onboarding")
                        }else{
                            isError = true
                            println("wrong pin")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Green)
            ) {
                Text(
                    text="Login",
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewMessageCard() {
}
