package com.lifelift.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * Workout Session Entity (Parent)
 */
enum class WorkoutType {
    STRENGTH,
    CARDIO
}

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // e.g., "Leg Day"
    val date: String, // ISO format
    val durationMinutes: Int,
    val notes: String = "",
    val type: WorkoutType = WorkoutType.STRENGTH // Default to Strength
)

/**
 * Exercise Performed Entity (Child of Workout)
 */
@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["workoutId"])]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutId: Long,
    val name: String, // Copied from reference, or custom
    val orderIndex: Int // To maintain order in list
)

/**
 * Set Performed Entity (Child of Exercise)
 */
@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["exerciseId"])]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exerciseId: Long,
    val reps: Int,
    val weight: Double,
    val isWarmup: Boolean = false
)

/**
 * Reference table for available exercises (e.g., "Bench Press", "Squat")
 * Used for the "Add Exercise" selector
 */
@Entity(tableName = "exercise_refs")
data class ExerciseRefEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "Chest", "Legs", etc.
    val defaultRestSeconds: Int = 60
)

// --- Relations POJOs ---

data class ExerciseWithSets(
    @androidx.room.Embedded val exercise: ExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId"
    )
    val sets: List<SetEntity>
) {
    val totalVolume: Double
        get() = sets.sumOf { it.reps * it.weight }
}

data class WorkoutWithExercises(
    @androidx.room.Embedded val workout: WorkoutEntity,
    @Relation(
        entity = ExerciseEntity::class,
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val exercises: List<ExerciseWithSets>
) {
    val totalVolume: Double
        get() = exercises.sumOf { it.totalVolume }
}
