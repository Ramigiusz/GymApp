package com.example.gymapp.data.dao

import androidx.room.*
import com.example.gymapp.data.model.Exercise

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}
