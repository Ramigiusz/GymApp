package com.example.gymapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.data.model.Exercise
import com.example.gymapp.data.model.ExerciseWithTags
import com.example.gymapp.data.model.Tag
import com.example.gymapp.data.model.ExerciseTagCrossRef
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).exerciseDao()

    // lista prostych ćwiczeń (do CRUD)
    var exercises by mutableStateOf<List<Exercise>>(emptyList())
        private set

    // ćwiczenia wraz z tagami
    var allExercisesWithTags by mutableStateOf<List<ExerciseWithTags>>(emptyList())
        private set

    // lista wszystkich tagów
    var allTags by mutableStateOf<List<Tag>>(emptyList())
        private set

    // pola do dodawania nowego ćwiczenia
    var newExerciseName by mutableStateOf("")
    var newExerciseDescription by mutableStateOf("")

    init {
        // zaladowanie domyślnych danych
        loadExercises()
        seedDefaults()
    }

    // załadowanie ćwiczeń
    fun loadExercises() = viewModelScope.launch {
        exercises = dao.getAllExercises()
    }

    fun addExercise(name: String, description: String = "") = viewModelScope.launch {
        dao.insertExercise(Exercise(name = name, description = description))
        loadExercises()
    }

    fun deleteExercise(exercise: Exercise) = viewModelScope.launch {
        dao.deleteExercise(exercise)
        loadExercises()
    }

    fun updateExercise(exercise: Exercise) = viewModelScope.launch {
        dao.updateExercise(exercise)
        loadExercises()
    }

    /** Dodaje domyślne ćwiczenia przy pierwszym uruchomieniu */
    private fun seedDefaults() = viewModelScope.launch {
        if (dao.getAllExercises().isEmpty()) {
            val squatId = dao.insertExercise(
                Exercise(name = "Przysiad", description = "Stań na szerokość barków..."))
                .toInt()
            val pushUpId = dao.insertExercise(
                Exercise(name = "Pompka", description = "Deska: opuść ciało..."))
                .toInt()

            loadExercises()
        }
    }
}
