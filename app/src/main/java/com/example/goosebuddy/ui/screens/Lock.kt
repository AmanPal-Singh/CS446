package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
    val btnModifier = Modifier.size(75.dp)

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
            var canSubmit by rememberSaveable { mutableStateOf(false) }


            fun addDigitToPin(digit: String){
                pin += digit
                canSubmit = pin.isNotEmpty()
            }

            @Composable
            fun drawPinRow(digits: Array<String>){
                return Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =  Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        shape = CircleShape,
                        onClick = { addDigitToPin(digits[0]) },
                        modifier = btnModifier,
                    ) {Text(digits[0])}
                    Button(
                        shape = CircleShape,
                        onClick = { addDigitToPin(digits[1]) },
                        modifier = btnModifier,
                    ) {Text(digits[1])}
                    Button(
                        shape = CircleShape,
                        onClick = { addDigitToPin(digits[2]) },
                        modifier = btnModifier,
                    ) {Text(digits[2])}
                }

            }
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

            Text(
                pin,
                textAlign= TextAlign.Center
            )
            if (isError){
                Text("PIN Invalid")
            }

            drawPinRow(digits = arrayOf("1", "2", "3"))
            drawPinRow(digits = arrayOf("4", "5", "6"))
            drawPinRow(digits = arrayOf("7", "8", "9"))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement =  Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    shape = CircleShape,
                    onClick = {
                        if (pin.isNotEmpty()) {
                            pin = pin.substring(0, pin.length - 1)
                        }
                        if (pin.isBlank()) {
                            canSubmit = false
                        }
                    },
                    modifier = btnModifier,
                ) {Text("x")}
                Button(
                    shape = CircleShape,
                    onClick = { addDigitToPin("0") },
                    modifier = btnModifier,
                ) {Text("0")}
                Button(
                    shape = CircleShape,
                    onClick = {
                        if(lockDao.getAll().isEmpty()){
                            lockDao.insert(Lock(0, pin.toInt()))
                        }else{
                            if (pin.isNotEmpty()) {
                                val valid_pin = lockDao.getAll()
                                if (valid_pin[0].value == pin.toInt()) {
                                    navController.navigate("home")
                                } else {
                                    isError = true
                                }
                            }
                        }
                    },
                    modifier = btnModifier
                ) {Text("Submit")}
            }
        }
    }
}
