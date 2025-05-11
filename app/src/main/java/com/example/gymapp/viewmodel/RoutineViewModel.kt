package com.example.gymapp.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.data.model.Routine
import com.example.gymapp.data.model.RoutineExercise
import com.example.gymapp.ui.screens.RoutineExerciseDraft
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val routineDao = AppDatabase.getDatabase(application).routineDao()
    private val routineExerciseDao = AppDatabase.getDatabase(application).routineExerciseDao()
    private val exerciseDao = AppDatabase.getDatabase(application).exerciseDao()

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

    fun saveRoutineWithExercises(
        name: String,
        routineId: Int?,
        exercises: List<RoutineExerciseDraft>
    ) {
        viewModelScope.launch {
            val routine: Routine
            val id: Int

            if (routineId == null || routineId == 0) {
                routine = Routine(name = name)
                id = routineDao.insertRoutineReturningId(routine).toInt()
            } else {
                routine = Routine(id = routineId, name = name)
                routineDao.updateRoutine(routine)
                id = routineId
                routineExerciseDao.deleteByRoutineId(id)
            }

            exercises.forEach { draft ->
                val exercise = RoutineExercise(
                    routineId = id,
                    exerciseId = draft.exercise.id,
                    sets = draft.sets,
                    reps = draft.reps,
                    rpe = draft.rpe,
                    restTimeMs = (draft.restMinutes * 60 + draft.restSeconds) * 1000
                )
                routineExerciseDao.insert(exercise)
            }

            loadRoutines()
        }
    }
    fun loadExercisesForRoutine(
        routineId: Int,
        onLoaded: (List<RoutineExerciseDraft>) -> Unit
    ) {
        viewModelScope.launch {
            val rels = routineExerciseDao.getByRoutineId(routineId)
            val allExercises = exerciseDao.getAllExercises()
            val drafts = rels.mapNotNull { rel ->
                val ex = allExercises.find { it.id == rel.exerciseId } ?: return@mapNotNull null
                RoutineExerciseDraft(
                    exercise = ex,
                    sets = rel.sets,
                    reps = rel.reps,
                    rpe = rel.rpe,
                    restMinutes = (rel.restTimeMs / 1000) / 60,
                    restSeconds = (rel.restTimeMs / 1000) % 60
                )
            }
            onLoaded(drafts)
        }
    }

}


