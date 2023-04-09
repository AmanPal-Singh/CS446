package com.example.goosebuddy.ui.screens.Habits

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.dao.HabitsDao
import com.example.goosebuddy.models.Habits
import org.burnoutcrew.reorderable.rememberReorderableLazyListState


@OptIn(ExperimentalMaterialApi::class)
class HabitsViewModel(db: AppDatabase) : ViewModel()  {
    var habitsDao: HabitsDao


    init {
        habitsDao = db.habitsDao()
    }



    private val _habits = MutableLiveData(habitsDao.getAll())
    val habits: LiveData<List<Habits>> = _habits



    private val _editingEnabled = MutableLiveData(false)
    val editingEnabled: LiveData<Boolean> = _editingEnabled

    private val _currentOrder = MutableLiveData(habitsDao.getAll().map { h -> h.id})
    val currentOrder: LiveData<List<Int>> = _currentOrder

    fun setCurrentOrder(newOrder: List<Int>)  {
        _currentOrder.value =  newOrder
    }

    fun setEditingEnabled(update: Boolean){
        _editingEnabled.value = update
    }

    fun refresheHabits(){
        _habits.value = habitsDao.getAll()
    }

    fun querySingle(item: Int): Habits {
        return habitsDao.loadSingle(item)
    }

    fun deleteHabit(habit: Habits){
        habitsDao.delete(habit)
    }

    fun updateHabit(habit: Habits){
        habitsDao.update(habit)
    }

    fun insertHabits(vararg habits: Habits){
        habitsDao.insertAll(*habits)
    }



}