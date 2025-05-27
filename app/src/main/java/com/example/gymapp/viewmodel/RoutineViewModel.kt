package com.example.gymapp.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.data.draft.ExerciseSetDraft
import com.example.gymapp.data.draft.RoutineExerciseDraft
import com.example.gymapp.data.model.Routine
import com.example.gymapp.data.model.RoutineExercise
import com.example.gymapp.data.model.RoutineExerciseSet
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val routineDao = db.routineDao()
    private val routineExerciseDao = db.routineExerciseDao()
    private val routineExerciseSetDao = db.routineExerciseSetDao()
    private val exerciseDao = db.exerciseDao()

    var routines by mutableStateOf(listOf<Routine>())
        private set

    init {
        viewModelScope.launch {
            // 1. Załaduj istniejące
            routines = routineDao.getAllRoutines()
            // 2. Jeśli żadnych rutyn, dopisz dwie przykładowe
            if (routines.isEmpty()) {
                // przykładowe nazwy
                val sample1 = Routine(name = "Push Routine")
                val sample2 = Routine(name = "Pull Routine")
                val sample3 = Routine(name = "Legs Routine")

                routineDao.insertRoutine(sample1)
                routineDao.insertRoutine(sample2)
                routineDao.insertRoutine(sample3)

                // odśwież listę
                routines = routineDao.getAllRoutines()
            }
        }
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
                val updated = routine.copy(name = name)
                routineDao.updateRoutine(updated)
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
            val id = if (routineId == null || routineId == 0) {
                // nowa rutyna
                routineDao.insertRoutineReturningId(Routine(name = name)).toInt()
            } else {
                // update istniejącej
                routineDao.updateRoutine(Routine(id = routineId, name = name))
                routineExerciseDao.deleteByRoutineId(routineId)
                routineId
            }

            // wstaw ćwiczenia i zestawy
            exercises.forEach { draft ->
                val relId = routineExerciseDao.insert(
                    RoutineExercise(
                        routineId = id,
                        exerciseId = draft.exercise.id,
                        restTimeMs = (draft.restMinutes * 60 + draft.restSeconds) * 1000
                    )
                ).toInt()

                val sets = draft.sets.map {
                    RoutineExerciseSet(
                        routineExerciseId = relId,
                        reps = it.reps,
                        rpe = it.rpe,
                        weight = it.weight,
                        completed = it.completed
                    )
                }
                routineExerciseSetDao.insertAll(sets)
            }

            loadRoutines()
        }
    }

    fun loadExercisesForRoutine(
        routineId: Int,
        onLoaded: (List<RoutineExerciseDraft>) -> Unit
    ) {
        viewModelScope.launch {
            val allExercises = exerciseDao.getAllExercises()
            val relations = routineExerciseDao.getByRoutineId(routineId)

            val drafts = relations.mapNotNull { rel ->
                val exercise = allExercises.find { it.id == rel.exerciseId } ?: return@mapNotNull null
                val sets = routineExerciseSetDao.getByRoutineExerciseId(rel.id)

                RoutineExerciseDraft(
                    exercise = exercise,
                    sets = sets.map {
                        ExerciseSetDraft(
                            reps = it.reps,
                            rpe = it.rpe,
                            weight = it.weight,
                            completed = it.completed
                        )
                    }.toCollection(mutableStateListOf()), // ⬅ KLUCZOWA ZMIANA
                    restMinutes = (rel.restTimeMs / 1000) / 60,
                    restSeconds = (rel.restTimeMs / 1000) % 60
                )
            }

            onLoaded(drafts)
        }
    }
}

