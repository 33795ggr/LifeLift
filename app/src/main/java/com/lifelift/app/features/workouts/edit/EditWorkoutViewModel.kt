package com.lifelift.app.features.workouts.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelift.app.core.data.local.entity.ExerciseEntity
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity
import com.lifelift.app.core.data.local.entity.SetEntity
import com.lifelift.app.core.data.local.entity.WorkoutEntity
import com.lifelift.app.core.data.repository.WorkoutRepository
import com.lifelift.app.core.util.DateUtils
import com.lifelift.app.features.workouts.SetUiData
import androidx.compose.runtime.Immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.lifelift.app.features.workouts.ImmutableList

// String-based UI models for editing to handle text input easily
@Immutable
data class EditSetUiState(
    val id: Long = System.nanoTime(),
    val reps: String = "",
    val weight: String = ""
)

@Immutable
data class EditExerciseUiState(
    val id: Long = System.nanoTime(),
    val name: String,
    val sets: ImmutableList<EditSetUiState> = ImmutableList(emptyList())
)

@Immutable
data class EditWorkoutUiState(
    val availableExercises: ImmutableList<ExerciseRefEntity> = ImmutableList(emptyList()),
    val addedExercises: ImmutableList<EditExerciseUiState> = ImmutableList(emptyList()),
    val selectedExerciseStats: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val initialName: String = "",
    val initialDuration: String = "",
    val initialNotes: String = ""
)

@HiltViewModel
class EditWorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EditWorkoutUiState())
    val uiState: StateFlow<EditWorkoutUiState> = _uiState.asStateFlow()
    
    private var currentWorkoutId: Long? = null
    
    init {
        loadExercises()
    }
    
    private fun loadExercises() {
        viewModelScope.launch {
            try {
                val exercises = workoutRepository.getAllExerciseRefs()
                _uiState.update { it.copy(availableExercises = ImmutableList(exercises)) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun loadWorkout(workoutId: Long) {
        currentWorkoutId = workoutId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val workoutWithExercises = workoutRepository.getWorkoutById(workoutId).first()
                
                val exercisesUi = workoutWithExercises.exercises.map { we ->
                    EditExerciseUiState(
                        name = we.exercise.name,
                        sets = ImmutableList(we.sets.mapIndexed { sIdx, it ->
                            EditSetUiState(
                                id = it.id,
                                reps = it.reps.toString(),
                                weight = it.weight.toString().removeSuffix(".0")
                            ) 
                        })
                    )
                }
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        addedExercises = ImmutableList(exercisesUi),
                        initialName = workoutWithExercises.workout.name,
                        initialDuration = workoutWithExercises.workout.durationMinutes.toString(),
                        initialNotes = workoutWithExercises.workout.notes
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun loadExerciseStats(exerciseName: String) {
        viewModelScope.launch {
            try {
                val sets = workoutRepository.getRecentSetsForExercise(exerciseName)
                if (sets.isNotEmpty()) {
                    val recentStats = sets.take(3).joinToString(", ") { "${it.weight}kg x ${it.reps}" }
                    _uiState.update { it.copy(selectedExerciseStats = "Recent: $recentStats") }
                } else {
                    _uiState.update { it.copy(selectedExerciseStats = "No history") }
                }
            } catch (e: Exception) {
                // ignore
            }
        }
    }
    
    fun clearSelectedExerciseStats() {
        _uiState.update { it.copy(selectedExerciseStats = null) }
    }
    
    fun addExercise(exerciseName: String, sets: List<SetUiData>) {
        // Convert SetUiData (from the sheet) to EditSetUiState
        val editSets = sets.map { 
            EditSetUiState(reps = it.reps.toString(), weight = it.weight.toString().removeSuffix(".0")) 
        }
        
        _uiState.update { 
            val updatedList = it.addedExercises.items + EditExerciseUiState(name = exerciseName, sets = ImmutableList(editSets))
            it.copy(addedExercises = ImmutableList(updatedList))
        }
    }
    
    fun removeExercise(index: Int) {
        _uiState.update {
            val updatedList = it.addedExercises.items.toMutableList().apply { removeAt(index) }
            it.copy(addedExercises = ImmutableList(updatedList))
        }
    }

    fun updateExerciseName(index: Int, name: String) {
        _uiState.update { state ->
            val updatedList = state.addedExercises.items.toMutableList()
            updatedList[index] = updatedList[index].copy(name = name)
            state.copy(addedExercises = ImmutableList(updatedList))
        }
    }
    
    fun addEmptyExercise() {
        _uiState.update {
            val newExercise = EditExerciseUiState(
                name = "",
                sets = ImmutableList(listOf(EditSetUiState(reps = "10", weight = "")))
            )
            it.copy(addedExercises = ImmutableList(it.addedExercises.items + newExercise))
        }
    }
    
    fun updateSet(exerciseIndex: Int, setIndex: Int, field: String, value: String) {
        _uiState.update { state ->
            val exercises = state.addedExercises.items.toMutableList()
            val exercise = exercises[exerciseIndex]
            val sets = exercise.sets.items.toMutableList()
            val set = sets[setIndex]
            
            val sanitized = if (value.count { it == '.' } > 1) value.dropLast(1) else value
            
            val updatedSet = when (field) {
                "reps" -> set.copy(reps = sanitized.filter { it.isDigit() })
                "weight" -> set.copy(weight = sanitized.filter { it.isDigit() || it == '.' })
                else -> set
            }
            
            sets[setIndex] = updatedSet
            exercises[exerciseIndex] = exercise.copy(sets = ImmutableList(sets))
            state.copy(addedExercises = ImmutableList(exercises))
        }
    }
    
    fun addSetToExercise(exerciseIndex: Int) {
        _uiState.update { state ->
            val exercises = state.addedExercises.items.toMutableList()
            val exercise = exercises[exerciseIndex]
            val currentSets = exercise.sets.items
            // Duplicate last set or add empty, but ensure NEW ID
            val lastSet = currentSets.lastOrNull()
            val newSet = lastSet?.copy(id = System.nanoTime()) ?: EditSetUiState(id = System.nanoTime())
            val newSets = currentSets + newSet 
            exercises[exerciseIndex] = exercise.copy(sets = ImmutableList(newSets))
            state.copy(addedExercises = ImmutableList(exercises))
        }
    }
    
    fun removeSetFromExercise(exerciseIndex: Int, setIndex: Int) {
        _uiState.update { state ->
            val exercises = state.addedExercises.items.toMutableList()
            val exercise = exercises[exerciseIndex]
            val sets = exercise.sets.items.toMutableList()
            if (sets.size > 0) {
                sets.removeAt(setIndex)
                exercises[exerciseIndex] = exercise.copy(sets = ImmutableList(sets))
                state.copy(addedExercises = ImmutableList(exercises))
            } else {
                state
            }
        }
    }
    
    fun updateWorkout(name: String, duration: Int, notes: String) {
        val workoutId = currentWorkoutId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // 1. Update Workout Entity
                val original = workoutRepository.getWorkoutById(workoutId).first().workout
                val updatedWorkout = original.copy(
                    name = name.ifBlank { "Workout" },
                    durationMinutes = duration,
                    notes = notes
                )
                workoutRepository.updateWorkout(updatedWorkout)
                
                // 2. Deep Edit: Delete OLD exercises/sets and Insert NEW ones
                // This is simple but destructive if IDs form relations. 
                // But for this app simpler is better.
                workoutRepository.deleteExercisesByWorkoutId(workoutId)
                
                // 3. Insert New Exercises and Sets
                _uiState.value.addedExercises.items.forEachIndexed { index, exerciseData ->
                    val exercise = ExerciseEntity(
                        workoutId = workoutId,
                        name = exerciseData.name,
                        orderIndex = index
                    )
                    val exerciseId = workoutRepository.insertExercise(exercise)
                    
                    exerciseData.sets.items.forEach { setData ->
                        val reps = setData.reps.toIntOrNull() ?: 0
                        val weight = setData.weight.toDoubleOrNull() ?: 0.0
                        
                        if (reps > 0 || weight > 0.0) {
                            val set = SetEntity(
                                exerciseId = exerciseId,
                                reps = reps,
                                weight = weight
                            )
                            workoutRepository.insertSet(set)
                        }
                    }
                }
                
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
                
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    fun resetSaveState() {
        _uiState.update { it.copy(isSaved = false) }
    }
}
