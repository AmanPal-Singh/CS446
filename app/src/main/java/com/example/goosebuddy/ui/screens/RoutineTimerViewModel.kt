package com.example.goosebuddy.ui.screens

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.ui.screens.Utility.formatTime
import java.util.concurrent.TimeUnit

object Utility {

    //time to countdown - 1hr - 60secs
    const val TIME_COUNTDOWN: Long = 60000L
    private const val TIME_FORMAT = "%02d:%02d"


    //convert time to milli seconds
    fun Long.formatTime(): String = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )

}

class RoutineTimerViewModel: ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    private val _time = MutableLiveData(Utility.TIME_COUNTDOWN.formatTime())
    val time: LiveData<String> = _time

    private val _progress = MutableLiveData(1.00F)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    fun handleCountdownTimer() {
        println("HELLO")
        if (isPlaying.value == true) {
            pauseTimer()
        } else {
            println("start timer")
            startTimer()
        }
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        handleTimerValues(false, Utility.TIME_COUNTDOWN.formatTime(), 1.0F)
    }

    private fun startTimer() {
        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(Utility.TIME_COUNTDOWN, 1000) {
            override fun onTick(millisRemaining: Long) {
                println("start timeraaa")
                val progressValue = millisRemaining.toFloat() / Utility.TIME_COUNTDOWN
                handleTimerValues(true, millisRemaining.formatTime(), progressValue)
                println(progressValue)
            }

            override fun onFinish() {
                pauseTimer()
            }
        }.start()
    }

    private fun handleTimerValues(isPlaying: Boolean, text: String, progress: Float) {
        _isPlaying.value = isPlaying
        _time.value = text
        _progress.value = progress
    }
}