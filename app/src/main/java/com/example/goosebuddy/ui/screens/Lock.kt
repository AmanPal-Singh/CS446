package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
            val viewModel by remember { mutableStateOf(LockViewModel(db))}
            val pin by viewModel.pin.observeAsState("")
            val isError by viewModel.isError.observeAsState(false)
            val canSubmit by viewModel.canSubmit.observeAsState(false)

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
                        onClick = { viewModel.addDigitToPin(digits[0]) },
                        modifier = btnModifier,
                        colors = ButtonDefaults.buttonColors(backgroundColor = LightBlue)
                    ) {Text(digits[0])}
                    Button(
                        shape = CircleShape,
                        onClick = { viewModel.addDigitToPin(digits[1]) },
                        modifier = btnModifier,
                        colors = ButtonDefaults.buttonColors(backgroundColor = LightBlue)
                    ) {Text(digits[1])}
                    Button(
                        shape = CircleShape,
                        onClick = { viewModel.addDigitToPin(digits[2]) },
                        modifier = btnModifier,
                        colors = ButtonDefaults.buttonColors(backgroundColor = LightBlue)
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
                    onClick = { viewModel.backspace() },
                    modifier = btnModifier,
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightBlue)
                ) {
                    Text("\u232b")
                }
                Button(
                    shape = CircleShape,
                    onClick = { viewModel.addDigitToPin("0") },
                    modifier = btnModifier,
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightBlue)
                ) {Text("0")}
                Button(
                    shape = CircleShape,
                    onClick = {
                        if(lockDao.getAll().isEmpty()){
                            lockDao.insert(Lock(0, pin.toInt()))
                        }else{
                            if(viewModel.verifyPin()){
                                navController.navigate("home")
                            }
                        }
                    },
                    modifier = btnModifier,
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightBlue)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tick),
                        contentDescription = "submit icon",
                        modifier = Modifier
                            .height(15.dp)
                            .width(15.dp)

                    )
                }
            }
        }
    }
}
