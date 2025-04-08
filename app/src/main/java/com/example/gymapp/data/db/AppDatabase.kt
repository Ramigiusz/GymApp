package com.example.gymapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymapp.data.dao.RoutineDao
import com.example.gymapp.data.model.Routine

@Database(entities = [Routine::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "colossus_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
