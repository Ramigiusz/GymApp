package com.example.gymapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gymapp.data.model.RoutineExercise

@Dao
interface RoutineExerciseDao {

    @Insert
    suspend fun insert(routineExercise: RoutineExercise): Long

    @Query("SELECT * FROM routine_exercises WHERE routineId = :routineId")
    suspend fun getByRoutineId(routineId: Int): List<RoutineExercise>

    @Query("DELETE FROM routine_exercises WHERE routineId = :routineId")
    suspend fun deleteByRoutineId(routineId: Int)
}
