// data/model/Tag.kt
package com.example.gymapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey val name: String
)

@Entity(
    tableName = "exercise_tag_cross_ref",
    primaryKeys = ["exerciseId", "tagName"]
)
data class ExerciseTagCrossRef(
    val exerciseId: Int,
    val tagName: String
)

data class ExerciseWithTags(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "id",            // kolumna z Exercise.id
        entityColumn = "name",          // kolumna z Tag.name
        associateBy = Junction(
            value = ExerciseTagCrossRef::class,
            parentColumn = "exerciseId",// kolumna w junction odnosząca się do Exercise.id
            entityColumn = "tagName"    // kolumna w junction odnosząca się do Tag.name
        )
    )
    val tags: List<Tag>
)
