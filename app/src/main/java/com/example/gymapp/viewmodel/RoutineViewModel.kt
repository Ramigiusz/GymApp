package com.example.gymapp.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.data.model.Routine
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val routineDao = AppDatabase.getDatabase(application).routineDao()

    var routines by mutableStateOf(listOf<Routine>())
        private set

    var newRoutineName by mutableStateOf("")

    init {
        loadRoutines()
    }

    fun loadRoutines() {
        viewModelScope.launch {
            routines = routineDao.getAllRoutines()
        }
    }

    fun addRoutine() {
        viewModelScope.launch {
            if (newRoutineName.isNotBlank()) {
                routineDao.insertRoutine(Routine(name = newRoutineName))
                loadRoutines()
                newRoutineName = ""
            }
        }
    }
}


