package com.example.goosebuddy.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.R
import com.example.goosebuddy.ui.theme.*

@Composable
fun LoginCard() {
    val context = LocalContext.current
    GooseBuddyTheme() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            var pin by remember { mutableStateOf("") }
            Text("Welcome back!\nPlease enter your pin to proceed.", textAlign = TextAlign.Center,)
            Image(
                painter = painterResource(id = R.drawable.goose),
                contentDescription = "Goose.",
                modifier = Modifier
                    .size(200.dp),
                colorFilter = ColorFilter.tint(Yellow)
            )
            TextField(
                placeholder = { Text("PIN") },
                value = pin,
                onValueChange = { newText -> pin = newText},
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                          print("hi")
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
    LoginCard()
}
