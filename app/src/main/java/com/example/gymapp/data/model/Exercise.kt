package com.example.gymapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String = "",
    val imageUri: String? = null,
    val videoUri: String? = null,
    val personalRecord: Float = 0f
)
