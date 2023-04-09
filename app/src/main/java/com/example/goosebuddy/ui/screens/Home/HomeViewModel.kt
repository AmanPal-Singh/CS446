package com.example.goosebuddy.ui.screens.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.AppDatabase
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel(db: AppDatabase): ViewModel() {
    private var _db: AppDatabase
    init {
        _db = db
    }
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

    private val _message = MutableLiveData(getDefaultGooseMessage())
    val message: LiveData<String> = _message

    fun getDefaultGooseMessage(): String {
        var calendarDao = _db.CalendarItemDao()
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

    fun getGreetingMessageAndTime(): Pair<String, String> {
        val current = LocalDateTime.now()
        val hour = current.hour
        val formatter = DateTimeFormatter.ofPattern("LLL dd, yyyy")
        val formatted = current.format(formatter)

        var greeting = "Good Morning!"
        println(hour)
        if (hour in 0..11) {
            greeting = "Good Morning!"
        } else if (hour in 12..16) {
            greeting = "Good Afternoon!"
        } else if (hour in 17..20) {
            greeting = "Good Evening!"
        } else if (hour in 21..23) {
            greeting = "Good Night!"
        }

        return Pair(formatted, greeting)
    }

    fun updateMessage(message: String) {
        println("UPDATING MESSAGE")
        _message.value = message
    }
}