package com.example.goosebuddy.ui.screens.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.dao.UserDataDao
import com.example.goosebuddy.models.UserData
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProfileViewModel(db: AppDatabase): ViewModel() {
    var userDataDao : UserDataDao
    init {
        userDataDao = db.userdataDao()
    }

    fun getUserData(): UserData {
        return userDataDao.getAll()
    }

    fun updateUserData(userData: UserData)  {
        userDataDao.update(userData)
    }

    private val _editingEnabled = MutableLiveData(false)
    val editingEnabled: LiveData<Boolean> = _editingEnabled

    fun setEditingEnabled(update: Boolean){
        _editingEnabled.value = update
    }

}