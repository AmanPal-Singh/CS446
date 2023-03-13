package com.example.goosebuddy
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


class LoginActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginCard()
        }
    }
}


@Composable
fun LoginCard() {
    val context = LocalContext.current
    Column{
        Image(painter = painterResource(id = R.drawable.goose), contentDescription = "Goose.")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }) {
            Text(text="login")
        }
    }
}

@Preview
@Composable
fun PreviewMessageCard() {
    LoginCard()
}