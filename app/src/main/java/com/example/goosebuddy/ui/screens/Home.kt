package com.example.goosebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.ui.shared.components.Goose
import com.example.goosebuddy.ui.shared.components.SpeechBubble
import com.example.goosebuddy.ui.theme.Beige
import com.example.goosebuddy.ui.theme.LightGrey
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

val options = arrayOf(
    "Got it.",
    "Thanks buddy.",
    "..."
)

val defaultMessages = listOf(
    "\nHonk Honk Honk!\n",
    "I may look cute but we are dangerous creatures! \nRespect our space",
    "\nDon't Policy 71!\n",
    "\nPhysics has a lot of study spaces!\n",
    "\nDon't forget your iClicker!\n",
    "\nBe nice!\n",
    "\nThank Mr. Goose\n",
    "\nBe sure to try different clubs! \n here is a variety of things to try!",
)

@Composable
fun Home(db:AppDatabase) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(LightGrey),
    ) {
        Spacer(modifier = Modifier.size(30.dp))
        DatedGreeting()
        Spacer(modifier = Modifier.size(70.dp))
        val text = remember { mutableStateOf(getDefaultGooseMessage(db)) }
        GooseAndSpeechBubble(text.value)
        SpeechOptions(options = options, text, defaultMessages)
    }
}

private fun getDefaultGooseMessage(db: AppDatabase): String {
    var calendarDao = db.CalendarItemDao()
    var current = kotlinx.datetime.LocalDate.now()
    var calendarItemsToday = calendarDao.getOnDate(current)
    var text = "\nHonk Honk!\n"
    run breaking@
    {
        calendarItemsToday.forEach { calendarItem ->
            text = "Honk! You have ${calendarItem.title} from \n ${calendarItem.startTime} - ${calendarItem.endTime}"
            return@breaking
        }
    }
    return text
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
        Goose(size = 225.dp, honkSound = true)
    }

}

@Composable
fun DatedGreeting() {
    val current = LocalDateTime.now()
    val hour = current.hour
    val formatter = DateTimeFormatter.ofPattern("LLL dd, yyyy")
    val formatted = current.format(formatter)

    var greeting = "Good Morning!"
    println(hour)
    if (hour in 0..11){
        greeting = "Good Morning!"
    } else if(hour in 12..16){
        greeting = "Good Afternoon!"
    } else if(hour in 17..20){
        greeting = "Good Evening!"
    } else if(hour in 21..23){
        greeting = "Good Night!"
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = formatted, fontSize = 24.sp, fontWeight = FontWeight.Medium)
        Text(greeting)
    }
}

@Composable
fun SpeechOptions(options: Array<String>, text: MutableState<String>, defaultMessages: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(35.dp)
    ) {
        options.forEach { it ->
            Button(
                onClick = {
                    val randomIndex = Random.nextInt(defaultMessages.size);
                    text.value = defaultMessages[randomIndex]
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