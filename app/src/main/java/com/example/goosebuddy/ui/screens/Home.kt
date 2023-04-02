package com.example.goosebuddy.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.goosebuddy.R
import com.example.goosebuddy.ui.theme.Beige
import com.example.goosebuddy.ui.theme.LightBlue
import com.example.goosebuddy.ui.theme.LightGrey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

val options = arrayOf(
    "Got it.",
    "Thanks buddy.",
    "..."
)

@Composable
@Preview
fun Home() {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(LightGrey),
    ) {
        Spacer(modifier = Modifier.size(30.dp))
        DatedGreeting()
        Spacer(modifier = Modifier.size(30.dp))
        SpeechBubble()
        Spacer(modifier = Modifier.size(30.dp))
        Goose()
        SpeechOptions(options = options)
    }
}

@Composable
fun DatedGreeting() {
    val current = LocalDateTime.now()
    val hour = current.hour
    val formatter = DateTimeFormatter.ofPattern("LLL dd, yyyy")
    val formatted = current.format(formatter)

    var greeting = "Good Morning!"

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(formatted)
        Text(greeting)
    }
}

@Composable
fun SpeechBubble(text: String = "Honk honk!\n" +
        "It’s raining today so don’t forget to bring an umbrella!\n more text \n" ) {
    val shape = RoundedCornerShape(50)
    Row(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.size(120.dp))
        Column() {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .border(shape = shape, width = 2.dp, color = Transparent)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .background(LightBlue)
                        .padding(10.dp),
                ) {
                    Text(text)
                }
            }
            SpeechBubbleTriangleArrow()
        }
        Spacer(modifier = Modifier.size(30.dp))
    }

}

@Composable
fun SpeechBubbleTriangleArrow(){
    Canvas(modifier = Modifier, onDraw = {
        val size = 20.dp.toPx()
        val offsetFromLeft = 10.dp.toPx()
        val trianglePath = Path().apply {
            // Moves to top center position
            moveTo(0f + offsetFromLeft, 0f)
            // Add line to bottom right corner
            lineTo(size + offsetFromLeft, 0f)
            // Add line to bottom left corner
            lineTo(size / 2f + offsetFromLeft, size)
        }
        drawPath(
            color = LightBlue,
            path = trianglePath
        )
    })

}

@Composable
fun Goose() {
    val imageModifier = Modifier
        .size(size = 225.dp)
    Image(
        painter = painterResource(id = R.drawable.buddy),
        contentDescription = "Goose Image",
        contentScale = ContentScale.Fit,
        modifier = imageModifier
    )
}

@Composable
fun SpeechOptions(options: Array<String>) {
    val shape = RoundedCornerShape(50)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(35.dp)
    ) {
        options.forEach { it ->
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(backgroundColor = Beige),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(it)
            }
        }
    }

}