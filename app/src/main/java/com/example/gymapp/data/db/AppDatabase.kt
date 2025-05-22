package com.example.gymapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymapp.data.dao.ExerciseDao
import com.example.gymapp.data.dao.RoutineDao
import com.example.gymapp.data.dao.RoutineExerciseDao
import com.example.gymapp.data.dao.RoutineExerciseSetDao
import com.example.gymapp.data.model.Exercise
import com.example.gymapp.data.model.Routine
import com.example.gymapp.data.model.RoutineExercise
import com.example.gymapp.data.model.RoutineExerciseSet
import com.example.gymapp.data.model.Tag
import com.example.gymapp.data.model.ExerciseTagCrossRef

@Database(entities = [Routine::class, Exercise::class, RoutineExercise::class, RoutineExerciseSet::class, Tag::class, ExerciseTagCrossRef::class], version = 5)
abstract class AppDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun routineExerciseDao(): RoutineExerciseDao
    abstract fun routineExerciseSetDao(): RoutineExerciseSetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "colossus_db"
                )
                    .fallbackToDestructiveMigration() //
                    .build().also { INSTANCE = it }
            }
        }
    }
}

