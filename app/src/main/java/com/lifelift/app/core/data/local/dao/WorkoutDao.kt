package com.lifelift.app.core.data.local.dao

import androidx.room.*
import com.lifelift.app.core.data.local.entity.ExerciseEntity
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity
import com.lifelift.app.core.data.local.entity.SetEntity
import com.lifelift.app.core.data.local.entity.WorkoutEntity
import com.lifelift.app.core.data.local.entity.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Workout operations
 */
@Dao
interface WorkoutDao {
    @Transaction
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE type = :type ORDER BY date DESC")
    fun getWorkoutsByType(type: com.lifelift.app.core.data.local.entity.WorkoutType): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :id")
    fun getWorkoutById(id: Long): Flow<WorkoutWithExercises>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: SetEntity): Long

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    // --- Exercise Reference Methods ---
    
    @Query("SELECT * FROM exercise_refs ORDER BY name ASC")
    suspend fun getAllExerciseRefs(): List<ExerciseRefEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseRef(exerciseRef: ExerciseRefEntity): Long

    // --- History / Progress ---

    /**
     * Gets the most recent sets for a given exercise name, ordered by workout date descending.
     * We limit to 50 to get a good chunk of history without loading everything.
     * Requires joining Sets -> Exercises -> Workouts to sort by Date.
     */
    @Query("""
        SELECT sets.* FROM sets 
        INNER JOIN exercises ON sets.exerciseId = exercises.id 
        INNER JOIN workouts ON exercises.workoutId = workouts.id 
        WHERE exercises.name = :exerciseName 
        ORDER BY workouts.date DESC, exercises.id DESC
        LIMIT 20
    """)
    suspend fun getRecentSetsForExercise(exerciseName: String): List<SetEntity>

    @Query("SELECT name FROM exercises GROUP BY name ORDER BY COUNT(*) DESC LIMIT :limit")
    suspend fun getTopExerciseNames(limit: Int): List<String>

    @Query("""
        SELECT sets.* FROM sets 
        INNER JOIN exercises ON sets.exerciseId = exercises.id 
        INNER JOIN workouts ON exercises.workoutId = workouts.id 
        WHERE exercises.name = :exerciseName 
        ORDER BY workouts.date ASC
    """)
    suspend fun getAllSetsForExercise(exerciseName: String): List<SetEntity>

    @Delete
    suspend fun deleteExerciseRef(exerciseRef: ExerciseRefEntity)
    
    @Query("DELETE FROM exercises WHERE workoutId = :workoutId")
    suspend fun deleteExercisesByWorkoutId(workoutId: Long)
}
