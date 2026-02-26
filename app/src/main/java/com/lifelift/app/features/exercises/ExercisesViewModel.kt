package com.lifelift.app.features.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity
import com.lifelift.app.core.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.Immutable
import javax.inject.Inject

@Immutable
data class ExerciseUiModel(
    val id: Long,
    val name: String,
    val category: String,
    val defaultRestSeconds: Int,
    val rawData: ExerciseRefEntity
)

@Immutable
data class ExercisesUiState(
    val exercises: List<ExerciseUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Assuming we can get a Flow or List from repository. 
                // We might need to add a method to get ALL ExerciseRefs if not exposed as Flow.
                // For now, let's assume we fetch them once or observe.
                // Since repository might not have a Flow<List<ExerciseRefEntity>>, we might need to update it.
                // I will update repository in next steps if needed.
                val exercises = workoutRepository.getAllExerciseRefs()
                
                val uiExercises = withContext(Dispatchers.Default) {
                    exercises.map { exercise ->
                        ExerciseUiModel(
                            id = exercise.id,
                            name = exercise.name,
                            category = exercise.category,
                            defaultRestSeconds = exercise.defaultRestSeconds,
                            rawData = exercise
                        )
                    }
                }
                
                _uiState.update { 
                    it.copy(
                        exercises = uiExercises,
                        isLoading = false 
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun addExercise(name: String, category: String) {
        viewModelScope.launch {
            try {
                val exercise = ExerciseRefEntity(
                    name = name,
                    category = category
                )
                workoutRepository.insertExerciseRef(exercise)
                loadExercises() // Refresh list
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun updateExercise(exercise: ExerciseRefEntity) {
        viewModelScope.launch {
            try {
                workoutRepository.insertExerciseRef(exercise)
                loadExercises()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    fun deleteExercise(exercise: ExerciseRefEntity) {
        viewModelScope.launch {
            try {
                workoutRepository.deleteExerciseRef(exercise)
                loadExercises()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}
