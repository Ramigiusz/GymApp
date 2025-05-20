package com.example.gymapp.data.draft

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.gymapp.data.model.Exercise

data class RoutineExerciseDraft(
    val exercise: Exercise,
    val sets: SnapshotStateList<ExerciseSetDraft> = mutableStateListOf(),
    var restMinutes: Int = 1,
    var restSeconds: Int = 0
)


data class ExerciseSetDraft(
    var reps: Int = 10,
    var rpe: Float = 8f,
    var weight: Float = 0f,
    var completed: Boolean = false
)
