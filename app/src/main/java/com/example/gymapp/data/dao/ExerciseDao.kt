package com.example.gymapp.data.dao

import androidx.room.*
import com.example.gymapp.data.model.Exercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(ex: Exercise): Long

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}
