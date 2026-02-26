package com.lifelift.app.core.data.repository

import com.lifelift.app.core.data.local.dao.WorkoutDao
import com.lifelift.app.core.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao
) {
    fun getAllWorkouts(): Flow<List<WorkoutWithExercises>> {
        return workoutDao.getAllWorkouts()
    }

    fun getWorkoutsByType(type: WorkoutType): Flow<List<WorkoutWithExercises>> {
        return workoutDao.getWorkoutsByType(type)
    }
    
    fun getWorkoutById(id: Long): Flow<WorkoutWithExercises> {
        return workoutDao.getWorkoutById(id)
    }
    
    suspend fun insertWorkout(workout: WorkoutEntity): Long {
        return workoutDao.insertWorkout(workout)
    }
    
    suspend fun insertExercise(exercise: ExerciseEntity): Long {
        return workoutDao.insertExercise(exercise)
    }
    
    suspend fun insertSet(set: SetEntity): Long {
        return workoutDao.insertSet(set)
    }
    
    suspend fun updateWorkout(workout: WorkoutEntity) {
        workoutDao.updateWorkout(workout)
    }
    
    suspend fun deleteWorkout(workout: WorkoutEntity) {
        workoutDao.deleteWorkout(workout)
    }
    
    // --- Reference Data ---
    
    suspend fun getAllExerciseRefs(): List<ExerciseRefEntity> {
        return workoutDao.getAllExerciseRefs()
    }
    
    suspend fun insertExerciseRef(exerciseRef: ExerciseRefEntity) = workoutDao.insertExerciseRef(exerciseRef)
    
    suspend fun getRecentSetsForExercise(exerciseName: String) = workoutDao.getRecentSetsForExercise(exerciseName)

    // Analytics (Calculated in-memory for now or via new DAO queries later)

    // Analytics (Calculated in-memory for now or via new DAO queries later)
    suspend fun getTotalVolume(): Double {
        // Implementation pending new analytics logic
        return 0.0
    }
    
    suspend fun deleteExerciseRef(exerciseRef: ExerciseRefEntity) {
        workoutDao.deleteExerciseRef(exerciseRef)
    }
    
    suspend fun deleteExercisesByWorkoutId(workoutId: Long) {
        workoutDao.deleteExercisesByWorkoutId(workoutId)
    }
}
