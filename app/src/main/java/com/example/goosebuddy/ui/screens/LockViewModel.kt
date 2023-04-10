package com.example.goosebuddy.ui.screens

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.AppDatabase

class LockViewModel(db: AppDatabase): ViewModel() {
    private var _pin = MutableLiveData("")
    private var _canSubmit = MutableLiveData(false)
    private var _isError = MutableLiveData(false)
    private var lockDao = db.lockDao()

    val pin: MutableLiveData<String>
        get() = _pin
    val canSubmit: MutableLiveData<Boolean>
        get() = _canSubmit
    val isError: MutableLiveData<Boolean>
        get() = _isError

    fun addDigitToPin(digit: String){
        _pin.value += digit
        canSubmit.value = pin.value?.isNotEmpty()
    }

    fun backspace(){
        if (pin.value?.isNotEmpty() == true) {
            pin.value = pin.value!!.substring(0, pin.value!!.length - 1)
        }
        if (pin.value?.isBlank() == true) {
            canSubmit.value = false
        }
    }

    fun verifyPin(): Boolean {
        val passcode = lockDao.getAll()
        Log.d("lockvm", passcode.toString())
        val ok = passcode[0].value == pin.value?.toInt()
        isError.value = ok
        return ok
    }


}