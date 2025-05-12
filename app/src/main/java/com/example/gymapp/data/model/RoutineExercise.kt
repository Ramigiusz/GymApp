package com.example.gymapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_exercises")
data class RoutineExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val routineId: Int,
    val exerciseId: Int,
    val sets: Int,
    val reps: Int,
    val rpe: Float,
    val restTimeMs: Int
)
