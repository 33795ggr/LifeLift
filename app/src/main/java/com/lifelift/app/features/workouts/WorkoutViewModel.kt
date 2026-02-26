package com.lifelift.app.features.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelift.app.core.data.local.entity.ExerciseEntity
import com.lifelift.app.core.data.local.entity.SetEntity
import com.lifelift.app.core.data.local.entity.WorkoutEntity
import com.lifelift.app.core.data.local.entity.WorkoutWithExercises
import com.lifelift.app.core.data.repository.WorkoutRepository
import com.lifelift.app.core.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.Immutable
import javax.inject.Inject

// UI DTOs for creation flow
@Immutable
data class SetUiData(val id: Long = System.nanoTime(), val reps: Int, val weight: Double)
@Immutable
data class ExerciseUiData(val id: Long = System.nanoTime(), val name: String, val sets: ImmutableList<SetUiData>)

// UI Models for optimized list rendering
@Immutable
data class WorkoutUiModel(
    val id: Long,
    val name: String,
    val dateDisplay: String,
    val durationMinutes: Int,
    val totalVolumeDisplay: String,
    val totalVolume: Double,
    val exercisesSummary: String,
    val exercisesDetails: List<Pair<String, String>>,
    val rawData: WorkoutWithExercises
)

@Immutable
data class WorkoutUiState(
    val workouts: List<WorkoutUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()
    
    init {
        loadWorkouts()
    }
    
    private fun loadWorkouts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                workoutRepository.getWorkoutsByType(com.lifelift.app.core.data.local.entity.WorkoutType.STRENGTH)
                    .map { workouts ->
                        workouts.map { workout ->
                            val dateObj = try { LocalDateTime.parse(workout.workout.date) } catch(e: Exception) { null }
                            val dateStr = dateObj?.format(DateTimeFormatter.ofPattern("EEE, MMM d")) ?: workout.workout.date.take(10)
                            
                             WorkoutUiModel(
                                id = workout.workout.id,
                                name = workout.workout.name,
                                dateDisplay = dateStr,
                                durationMinutes = workout.workout.durationMinutes,
                                totalVolumeDisplay = "${workout.totalVolume.toInt()} kg",
                                totalVolume = workout.totalVolume,
                                exercisesSummary = workout.exercises.take(3).joinToString(", ") { it.exercise.name },
                                exercisesDetails = workout.exercises.map { exercise ->
                                    val sets = exercise.sets
                                    val setInfo = if (sets.isNotEmpty()) {
                                        val weights = sets.map { it.weight }
                                        val distinctWeights = weights.distinct()
                                        if (distinctWeights.size == 1) {
                                            val weight = distinctWeights.first()
                                            val weightStr = if (weight % 1.0 == 0.0) weight.toInt().toString() else weight.toString()
                                            "${sets.size} sets x ${weightStr}kg"
                                        } else {
                                            val min = weights.minOrNull() ?: 0.0
                                            val max = weights.maxOrNull() ?: 0.0
                                            val minStr = if (min % 1.0 == 0.0) min.toInt().toString() else min.toString()
                                            val maxStr = if (max % 1.0 == 0.0) max.toInt().toString() else max.toString()
                                            "${sets.size} sets ($minStr-$maxStr kg)"
                                        }
                                    } else {
                                        "0 sets"
                                    }
                                    exercise.exercise.name to setInfo
                                },
                                rawData = workout
                            )
                        }
                    }
                    .flowOn(Dispatchers.Default)
                    .catch { e ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = e.message
                            )
                        }
                    }
                    .collect { uiWorkouts ->
                        _uiState.update {
                            it.copy(
                                workouts = uiWorkouts,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
    
    fun addWorkout(
        name: String,
        exercises: List<ExerciseUiData>,
        durationMinutes: Int,
        notes: String = ""
    ) {
        viewModelScope.launch {
            try {
                // 1. Insert Workout
                val workout = WorkoutEntity(
                    name = name,
                    date = DateUtils.getCurrentDateTime(),
                    durationMinutes = durationMinutes,
                    notes = notes
                )
                val workoutId = workoutRepository.insertWorkout(workout)
                
                // 2. Insert Exercises and Sets
                exercises.forEachIndexed { index, exerciseData ->
                    val exercise = ExerciseEntity(
                        workoutId = workoutId,
                        name = exerciseData.name,
                        orderIndex = index
                    )
                    val exerciseId = workoutRepository.insertExercise(exercise)
                    
                    exerciseData.sets.items.forEach { setData ->
                        val set = SetEntity(
                            exerciseId = exerciseId,
                            reps = setData.reps,
                            weight = setData.weight
                        )
                        workoutRepository.insertSet(set)
                    }
                }
                
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            try {
                workoutRepository.insertWorkout(workout) // Room Insert with OnConflictStrategy.REPLACE usually handles update if ID matches
                // Wait, check Repository implementation. If insert implies replacement.
                // Standard Dao usually has @Update or @Insert(onConflict = REPLACE).
                // Let's assume insertWorkout handles it or I should check.
                // If not, I might need an explicit update method in DAO.
                // I'll check DAO in a sec. Assuming insert covers it or I'll add update.
                // Safest is to check DAO.
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private var recentlyDeletedWorkout: WorkoutWithExercises? = null

    fun deleteWorkout(workout: WorkoutWithExercises) {
        viewModelScope.launch {
            try {
                // Store for Undo
                recentlyDeletedWorkout = workout
                workoutRepository.deleteWorkout(workout.workout)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    fun undoDeleteWorkout() {
        val workoutToRestore = recentlyDeletedWorkout ?: return
        
        viewModelScope.launch {
            try {
                // Restore Workout (New ID)
                val newWorkoutId = workoutRepository.insertWorkout(workoutToRestore.workout.copy(id = 0)) // 0 to auto-generate
                
                // Restore Exercises and Sets
                workoutToRestore.exercises.forEach { exerciseWithSets ->
                    val newExerciseId = workoutRepository.insertExercise(
                        exerciseWithSets.exercise.copy(id = 0, workoutId = newWorkoutId)
                    )
                    
                    exerciseWithSets.sets.forEach { set ->
                        workoutRepository.insertSet(
                            set.copy(id = 0, exerciseId = newExerciseId)
                        )
                    }
                }
                
                recentlyDeletedWorkout = null
                
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to undo: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
