package com.lifelift.app.core.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "programs")
data class ProgramEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = ""
)

@Entity(
    tableName = "program_exercises",
    foreignKeys = [
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["programId"])]
)
data class ProgramExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val programId: Long,
    val name: String,
    val orderIndex: Int,
    val targetSets: Int = 3,
    val targetReps: Int = 10
)

data class ProgramWithExercises(
    @Embedded val program: ProgramEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "programId"
    )
    val exercises: List<ProgramExerciseEntity>
)
