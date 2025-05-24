package com.example.gymapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_logs")
data class ExerciseLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseId: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val reps: Int,
    val weight: Float,
    val rpe: Float
)
