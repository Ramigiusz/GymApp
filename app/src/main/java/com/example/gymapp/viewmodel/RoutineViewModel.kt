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

    fun addRoutine(name: String) {
        viewModelScope.launch {
            val routine = Routine(name = name)
            routineDao.insertRoutine(routine)
            loadRoutines()
        }
    }

    fun updateRoutine(id: Int, name: String) {
        viewModelScope.launch {
            val routine = routines.find { it.id == id }
            if (routine != null) {
                val updatedRoutine = routine.copy(name = name)
                routineDao.updateRoutine(updatedRoutine)
                loadRoutines()
            }
        }
    }
    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            routineDao.deleteRoutine(routine)
            loadRoutines()
        }
    }
}


