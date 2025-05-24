package com.example.gymapp.data.dao

import androidx.room.*
import com.example.gymapp.data.model.ExerciseLog

@Dao
interface ExerciseLogDao {

    @Insert
    suspend fun insert(log: ExerciseLog)

    @Query("SELECT * FROM exercise_logs WHERE exerciseId = :exerciseId ORDER BY weight DESC, reps DESC LIMIT 1")
    suspend fun getBestLogForExercise(exerciseId: Int): ExerciseLog?

    @Query("SELECT * FROM exercise_logs WHERE exerciseId = :exerciseId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastLogForExercise(exerciseId: Int): ExerciseLog?

    @Query("SELECT * FROM exercise_logs WHERE exerciseId = :exerciseId ORDER BY timestamp DESC")
    suspend fun getAllLogsForExercise(exerciseId: Int): List<ExerciseLog>
}
