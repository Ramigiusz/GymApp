package com.example.gymapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.data.model.Exercise
import com.example.gymapp.data.model.Tag
import com.example.gymapp.data.model.ExerciseTagCrossRef
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
        viewModelScope.launch {
            if (dao.getAllExercises().isEmpty()) {
                // 1) Dodaj dwa ćwiczenia
                val squatId = dao.insertExercise(
                    Exercise(name="Przysiad",
                        description="Stań na szerokość barków, schodź w dół, pilnując kolan.")
                ).toInt()
                val pushUpId = dao.insertExercise(
                    Exercise(name="Pompka",
                        description="Deska: opuść ciało, aż łokcie zgięte pod kątem 90°.")
                ).toInt()
                // 2) Dodaj tagi
                listOf("Nogi","Klatka","Tricepsy","Plecy","Bicepsy","Barki").forEach {
                    dao.insertTag(Tag(it))
                }
                // 3) Połącz ćwiczenia z tagami
                dao.insertCrossRef(ExerciseTagCrossRef(squatId, "Nogi"))
                dao.insertCrossRef(ExerciseTagCrossRef(pushUpId, "Klatka"))
                dao.insertCrossRef(ExerciseTagCrossRef(pushUpId, "Tricepsy"))
            }
        }
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
