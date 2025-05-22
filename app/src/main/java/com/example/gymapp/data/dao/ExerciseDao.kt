package com.example.gymapp.data.dao

import androidx.room.*
import com.example.gymapp.data.model.Exercise
import com.example.gymapp.data.model.ExerciseTagCrossRef
import com.example.gymapp.data.model.ExerciseWithTags
import com.example.gymapp.data.model.Tag

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    @Transaction
    @Query("SELECT * FROM exercises")
    fun getAllExercisesWithTags(): List<ExerciseWithTags>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(ex: Exercise): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(ref: ExerciseTagCrossRef)

    @Query("DELETE FROM exercise_tag_cross_ref WHERE exerciseId = :id")
    suspend fun clearTagsForExercise(id: Int)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}
