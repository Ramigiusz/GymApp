package com.example.gymapp.data.dao

import androidx.room.*
import com.example.gymapp.data.model.RoutineExerciseSet

@Dao
interface RoutineExerciseSetDao {
    @Insert
    suspend fun insert(set: RoutineExerciseSet): Long

    @Insert
    suspend fun insertAll(sets: List<RoutineExerciseSet>)

    @Query("SELECT * FROM routine_exercise_sets WHERE routineExerciseId = :id")
    suspend fun getByRoutineExerciseId(id: Int): List<RoutineExerciseSet>

    @Query("DELETE FROM routine_exercise_sets WHERE routineExerciseId = :id")
    suspend fun deleteByRoutineExerciseId(id: Int)
}
