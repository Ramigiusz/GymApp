package com.example.gymapp.data.dao

import androidx.room.*
import com.example.gymapp.data.model.Routine

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routines")
    suspend fun getAllRoutines(): List<Routine>

    @Insert
    suspend fun insertRoutine(routine: Routine)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Update
    suspend fun updateRoutine(routine: Routine)
}
