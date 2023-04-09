package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.ui.screens.Home.HomeViewModel
import com.example.goosebuddy.ui.screens.Utility.formatTime
import com.example.goosebuddy.ui.shared.components.DefaultGoose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.theme.Beige
import com.example.goosebuddy.ui.theme.LightGrey
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random



@Composable
fun Home(db:AppDatabase) {
    val viewModel by remember {
        mutableStateOf(HomeViewModel(db))
    }

    val message by viewModel.message.observeAsState(viewModel.getDefaultGooseMessage())
    val (time, greeting) = viewModel.getGreetingMessageAndTime()

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(LightGrey),
    ) {
        Spacer(modifier = Modifier.size(30.dp))
        DatedGreeting(time, greeting)
        Spacer(modifier = Modifier.size(70.dp))
        GooseAndSpeechBubble(message)
        SpeechOptions(options = viewModel.options, viewModel.defaultMessages, viewModel)
    }
}

@Composable
fun GooseAndSpeechBubble(text: String){
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SpeechBubble(text, includeLeftSpacing = false)
        Spacer(modifier = Modifier.size(30.dp))
        DefaultGoose().decorate()
    }

}

@Composable
fun DatedGreeting(formattedTime: String, greeting: String) {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = formattedTime, fontSize = 24.sp, fontWeight = FontWeight.Medium)
        Text(greeting)
    }
}

@Composable
fun SpeechOptions(options: Array<String>, defaultMessages: List<String>, viewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(35.dp)
    ) {
        options.forEach { it ->
            Button(
                onClick = {
                    val randomIndex = Random.nextInt(defaultMessages.size)
                    viewModel.updateMessage(defaultMessages[randomIndex])
                  },
                colors = ButtonDefaults.buttonColors(backgroundColor = Beige),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(it)
            }
        }
    }
}