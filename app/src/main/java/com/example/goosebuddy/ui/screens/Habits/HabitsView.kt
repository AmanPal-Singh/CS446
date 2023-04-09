package com.example.goosebuddy.ui.screens.Habits

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.goosebuddy.AppDatabase


@Composable
fun HabitsView(db: AppDatabase) {
    val viewModel by remember {
        mutableStateOf(HabitsViewModel(db))
    }

}