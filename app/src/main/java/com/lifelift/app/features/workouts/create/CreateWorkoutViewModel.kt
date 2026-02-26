package com.lifelift.app.features.workouts.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelift.app.core.data.local.entity.ExerciseEntity
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity
import com.lifelift.app.core.data.local.entity.SetEntity
import com.lifelift.app.core.data.local.entity.WorkoutEntity
import com.lifelift.app.core.data.repository.WorkoutRepository
import com.lifelift.app.core.util.DateUtils
import com.lifelift.app.features.workouts.ExerciseUiData
import com.lifelift.app.features.workouts.SetUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.Immutable
import com.lifelift.app.features.workouts.ImmutableList
import javax.inject.Inject


enum class CreationStep {
    SETUP, EXERCISES
}

@Immutable
data class CreateWorkoutUiState(
    val step: CreationStep = CreationStep.SETUP,
    val currentExerciseIndex: Int = 0,
    val availableExercises: List<ExerciseRefEntity> = emptyList(),
    val addedExercises: ImmutableList<ExerciseUiData> = ImmutableList(emptyList()),
    val selectedExerciseStats: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CreateWorkoutUiState())
    val uiState: StateFlow<CreateWorkoutUiState> = _uiState.asStateFlow()
    
    init {
        loadExercises()
    }
    
    private fun loadExercises() {
        viewModelScope.launch {
            try {
                val exercises = workoutRepository.getAllExerciseRefs()
                _uiState.update { it.copy(availableExercises = exercises) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    // --- Navigation Logic ---
    fun proceedToExercises() {
        _uiState.update { it.copy(step = CreationStep.EXERCISES) }
        // Ensure at least one empty exercise exists if list is empty
        if (_uiState.value.addedExercises.items.isEmpty()) {
            addEmptyExercise()
        }
    }

    fun goBackToSetup() {
        _uiState.update { it.copy(step = CreationStep.SETUP) }
    }
    
    fun nextExercise() {
        _uiState.update { state ->
            val nextIndex = state.currentExerciseIndex + 1
            // If next index is beyond current list, add new empty exercise
            if (nextIndex >= state.addedExercises.size) {
                 val newExercise = ExerciseUiData(
                    name = "",
                    sets = ImmutableList(listOf(SetUiData(id = System.nanoTime(), reps = 10, weight = 0.0)))
                )
                state.copy(
                    currentExerciseIndex = nextIndex,
                    addedExercises = ImmutableList(state.addedExercises.items + newExercise)
                )
            } else {
                state.copy(currentExerciseIndex = nextIndex)
            }
        }
    }
    
    fun previousExercise() {
        _uiState.update { state ->
            if (state.currentExerciseIndex > 0) {
                state.copy(currentExerciseIndex = state.currentExerciseIndex - 1)
            } else {
                // If at first exercise, maybe go back to Setup?
                // Or let UI handle "Back" button to call goBackToSetup()
                state
            }
        }
    }
    
    // --- Data Logic (Adapted for single-index modification) ---

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
    
    fun addEmptyExercise() {
        _uiState.update {
            val newExercise = ExerciseUiData(
                name = "",
                sets = ImmutableList(listOf(SetUiData(id = System.nanoTime(), reps = 10, weight = 0.0)))
            )
            it.copy(addedExercises = ImmutableList(it.addedExercises.items + newExercise))
        }
    }
    
    // Updates Current (or specific) Exercise Name
    // Updates Current (or specific) Exercise Name
    fun updateExerciseName(index: Int, name: String) {
        val currentList = _uiState.value.addedExercises.items.toMutableList()
        if (index >= currentList.size) return
        
        // Immediate UI Update
        currentList[index] = currentList[index].copy(name = name)
        _uiState.value = _uiState.value.copy(addedExercises = ImmutableList(currentList))
        
        // Auto-fill Logic
        viewModelScope.launch {
            try {
                val recentSetsFull = workoutRepository.getRecentSetsForExercise(name)
                if (recentSetsFull.isNotEmpty()) {
                    // Group by exerciseId matches sets from ONE workout instance.
                    // recentSetsFull is sorted by Date DESC, so the first exerciseId found is the latest.
                    val latestExerciseId = recentSetsFull.first().exerciseId
                    val lastSessionSets = recentSetsFull.filter { set -> set.exerciseId == latestExerciseId }
                    
                    if (lastSessionSets.isNotEmpty()) {
                        val filledSets = lastSessionSets.map { set -> SetUiData(reps = set.reps, weight = set.weight) }
                        
                        // Apply update safely
                        _uiState.update { currentState ->
                             val newList = currentState.addedExercises.items.toMutableList()
                             // Verify we are still editing the same exercise (race condition check)
                             if (index < newList.size && newList[index].name == name) {
                                  newList[index] = newList[index].copy(sets = ImmutableList(filledSets))
                             }
                             currentState.copy(addedExercises = ImmutableList(newList))
                        }
                    }
                }
            } catch (e: Exception) {
               // Fail silently
            }
        }
    }

    fun addSet(exerciseIndex: Int) {
        _uiState.update { state ->
            val updatedExercises = state.addedExercises.items.toMutableList()
            if (exerciseIndex < updatedExercises.size) {
                val exercise = updatedExercises[exerciseIndex]
                val previousSet = exercise.sets.items.lastOrNull()
                val newSet = previousSet?.copy(id = System.nanoTime()) ?: SetUiData(id = System.nanoTime(), reps = 10, weight = 0.0)
                
                updatedExercises[exerciseIndex] = exercise.copy(sets = ImmutableList(exercise.sets.items + newSet))
            }
            state.copy(addedExercises = ImmutableList(updatedExercises))
        }
    }

    fun removeSet(exerciseIndex: Int, setIndex: Int) {
        _uiState.update { state ->
            val updatedExercises = state.addedExercises.items.toMutableList()
            if (exerciseIndex < updatedExercises.size) {
                val exercise = updatedExercises[exerciseIndex]
                if (exercise.sets.items.size > 1) { 
                    val updatedSets = exercise.sets.items.toMutableList()
                    updatedSets.removeAt(setIndex)
                    updatedExercises[exerciseIndex] = exercise.copy(sets = ImmutableList(updatedSets))
                }
            }
            state.copy(addedExercises = ImmutableList(updatedExercises))
        }
    }

    fun updateSet(exerciseIndex: Int, setIndex: Int, reps: Int, weight: Double) {
        _uiState.update { state ->
            val updatedExercises = state.addedExercises.items.toMutableList()
            if (exerciseIndex < updatedExercises.size) {
                val exercise = updatedExercises[exerciseIndex]
                val updatedSets = exercise.sets.items.toMutableList()
                if (setIndex < updatedSets.size) {
                    updatedSets[setIndex] = updatedSets[setIndex].copy(reps = reps, weight = weight)
                    updatedExercises[exerciseIndex] = exercise.copy(sets = ImmutableList(updatedSets))
                }
            }
            state.copy(addedExercises = ImmutableList(updatedExercises))
        }
    }
    
    fun removeExercise(index: Int) {
         _uiState.update {
            val updatedList = it.addedExercises.items.toMutableList()
            if (index < updatedList.size) {
                updatedList.removeAt(index)
            }
            // Logic to adjust current index if we deleted the current one or one before it
            var newIndex = it.currentExerciseIndex
            if (index <= it.currentExerciseIndex && newIndex > 0) {
                 newIndex--
            }
            it.copy(addedExercises = ImmutableList(updatedList), currentExerciseIndex = newIndex)
        }
    }

    fun createAndSelectExercise(name: String, category: String, sets: Int, reps: Int, weight: Double) {
        viewModelScope.launch {
            try {
                workoutRepository.insertExerciseRef(ExerciseRefEntity(name = name, category = category))
                val exercises = workoutRepository.getAllExerciseRefs()
                
                // Update the CURRENT exercise being edited to this new name
                _uiState.update { state ->
                    val updatedList = state.addedExercises.items.toMutableList()
                    val idx = state.currentExerciseIndex
                    if (idx < updatedList.size) {
                         updatedList[idx] = updatedList[idx].copy(name = name)
                    }
                    state.copy(
                        availableExercises = exercises,
                        addedExercises = ImmutableList(updatedList)
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    fun saveWorkout(name: String, duration: Int, notes: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Filter out empty exercises
                val validExercises = _uiState.value.addedExercises.items.filter { it.name.isNotBlank() }
                
                if (validExercises.isEmpty()) {
                     _uiState.update { it.copy(isLoading = false) } // Nothing to save
                     return@launch
                }

                val workout = WorkoutEntity(
                    name = name.ifBlank { "Workout" },
                    date = DateUtils.getCurrentDateTime(),
                    durationMinutes = duration,
                    notes = notes
                )
                val workoutId = workoutRepository.insertWorkout(workout)
                
                validExercises.forEachIndexed { index, exerciseData ->
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
