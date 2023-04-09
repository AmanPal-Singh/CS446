package com.example.goosebuddy.ui.screens

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.ui.screens.Utility.formatTime
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object Utility {
    //time to countdown - 1hr - 60secs
    private const val TIME_FORMAT = "%02d:%02d"
    
    //convert time to milli seconds
    fun Long.formatTime(): String = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )

}

class RoutineTimerViewModel(duration: Duration): ViewModel() {
    private var initialDuration: Duration
    private var _duration: Duration
    init {
        initialDuration = duration
        _duration = duration
    }

    private var countDownTimer: CountDownTimer? = null

    private val _time = MutableLiveData(duration.inWholeMilliseconds.formatTime())
    val time: LiveData<String> = _time

    private val _progress = MutableLiveData(1.00F)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    fun handleCountdownTimer() {
        if (isPlaying.value == true) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        handleTimerValues(false, _duration.inWholeMilliseconds.formatTime(), 1.0F)
    }

    private fun pauseTimer() {
        _duration = (initialDuration.inWholeMilliseconds * progress.value!!).toLong().milliseconds
        countDownTimer?.cancel()
        _progress.value?.let { handleTimerValues(false, _duration.inWholeMilliseconds.formatTime(), it) }
    }

    private fun startTimer() {
        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(_duration.inWholeMilliseconds, 10) {
            override fun onTick(millisRemaining: Long) {
                val progressValue = millisRemaining.toFloat() / initialDuration.inWholeMilliseconds
                handleTimerValues(true, millisRemaining.formatTime(), progressValue)
            }

            override fun onFinish() {
                stopTimer()
            }
        }.start()
    }

    private fun handleTimerValues(isPlaying: Boolean, text: String, progress: Float) {
        _isPlaying.value = isPlaying
        _time.value = text
        _progress.value = progress
    }
}