package com.example.goosebuddy.ui.screens.Onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.dao.HabitsDao
import com.example.goosebuddy.dao.RoutinesDao
import com.example.goosebuddy.dao.UserDataDao
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.models.Routines
import com.example.goosebuddy.models.Subroutines
import com.example.goosebuddy.models.UserData
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OnboardingViewModel(db: AppDatabase): ViewModel() {

    val routinesDao: RoutinesDao
    val userDataDao: UserDataDao
    val habitsDao: HabitsDao

    init {
        routinesDao = db.routinesDao()
        userDataDao = db.userdataDao()
        habitsDao = db.habitsDao()
    }

    fun insertUserData(userData: UserData){
        userDataDao.insert(userData)
    }

    fun clearHabits(){
        habitsDao.deleteAll()
    }

    fun insertHabits(vararg habits: Habits){
        habitsDao.insertAll(*habits)
    }

    fun getHabits(): List<Habits> {
        return habitsDao.getAll()
    }


    val onboardingSteps = arrayOf(
        OnboardingStep("welcome"),
        OnboardingStep("name"),
        OnboardingStep("wat"),
        OnboardingStep("year"),
        OnboardingStep("residence"),
        OnboardingStep("schedule"),
        OnboardingStep("recommendations"),
        OnboardingStep("submit"),
    )

    val suggestedHabit = mapOf(
        "hasRoommates" to listOf(
            Habits(0, "Chore schedule", "Set up a chore schedule with your roommates and stick with it!", schedule = "Weekly"),
            Habits(0, "Hang out with roommates", "It's important to have a good relationship with your roommates!", schedule = "Weekly"),
        ),
        "onStudentRes" to listOf(
            Habits(0, "Do Laundry", "Doing laundry is an important task for your Hygiene!", schedule = "Weekly"),
            Habits(0, "Shower", "Being clean is an important for your (and your friends') health!"),
        ),
        "firstTimeAlone" to listOf(
            Habits(0, "Call your parents", "Your parents miss you! Let them know how you are doing!"),
            Habits(0, "Go on a walk", "Get your daily steps in!"),
        )
    )

    fun getSuggestedHabit(habitName: String): List<Habits>? {
        return suggestedHabit["hasRoommates"]
    }

    private val pomodoroSub1 = Subroutines(0, 1, "Study 1", "Study time!", false, 3)
    private val pomodoroSub2 = Subroutines(0, 1, "Break 1", "Have some water!", false, 3)
    private val pomodoroSub3 = Subroutines(0, 1, "Study 2", "Study time!", false, 3)
    private val pomodoroSub4 = Subroutines(0, 1, "Break 2", "Grab a snack!", false, 3)
    private val pomodoroSub5 = Subroutines(0, 1, "Study 3", "Study time", false, 3)
    private val pomodoroRoutine = Routines(1, "Pomodoro", "Time to study for MATH 135!", 0, 5)

    private fun getPomodoroRoutine(): Routines {
        return pomodoroRoutine
    }

    private  fun getPomodoroSubroutines(): List<Subroutines> {
        return listOf(pomodoroSub1, pomodoroSub2, pomodoroSub3, pomodoroSub4, pomodoroSub5)
    }

    fun createPomodoroSubroutines() {
        routinesDao.insert(getPomodoroRoutine(), getPomodoroSubroutines())
    }

    val welcomeMsg = "Welcome to GooseBuddy!\n\n" +
            "University may be overwhelming\n"+
            "but I am here to help you navigate through university by helping you build\n" +
            "good habits and reach your goals!\n\n" +
            "Are you ready to get started on your journey?"

    fun getWelcomeMessage(): String {
        return welcomeMsg
    }

}