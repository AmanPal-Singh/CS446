package com.example.goosebuddy.ui.screens

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.goosebuddy.AppDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    fun updatePin(): Boolean{
        val db = Firebase.firestore
        if(lockDao.getAll().isEmpty()){
            val user = hashMapOf(
                "user" to "thereIsOnlyOne",
                "pin" to pin.value
            )
            var ok = true
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d("firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                    ok = false
                }
                .addOnFailureListener { e ->
                    Log.w("firestore", "Error adding document", e)
                    ok = false
                }
            return ok
        }
        return true
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
        var ok = false
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("firestore", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("firestore", "Error getting documents.", exception)
            }
        val passcode = lockDao.getAll()
        Log.d("lockvm", passcode.toString())
        ok = passcode[0].value == pin.value?.toInt()
        isError.value = ok
        return ok
    }


}