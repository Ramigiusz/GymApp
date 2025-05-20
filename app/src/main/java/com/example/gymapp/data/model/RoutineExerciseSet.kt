package com.example.gymapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_exercise_sets")
data class RoutineExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val routineExerciseId: Int,
    val reps: Int,
    val rpe: Float,
    val weight: Float,
    val completed: Boolean = false
)
