package com.example.gymapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.data.model.Exercise
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).exerciseDao()

    var exercises by mutableStateOf(listOf<Exercise>())
        private set

    var newExerciseName by mutableStateOf("")
    var newExerciseDescription by mutableStateOf("")

    init {
        loadExercises()
    }

    fun loadExercises() {
        viewModelScope.launch {
            exercises = dao.getAllExercises()
        }
    }

    fun addExercise(name: String, description: String = "") {
        viewModelScope.launch {
            dao.insertExercise(Exercise(name = name, description = description))
            loadExercises()
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            dao.deleteExercise(exercise)
            loadExercises()
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            dao.updateExercise(exercise)
            loadExercises()
        }
    }
}
