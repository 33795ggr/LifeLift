package com.lifelift.app.features.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelift.app.core.data.local.entity.WorkoutWithExercises
import com.lifelift.app.core.data.repository.VitaminRepository
import com.lifelift.app.core.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.Immutable
import javax.inject.Inject

@Immutable
data class AnalyticsUiState(
    // Daily Stats
    val dailyWorkoutsCount: Int = 0,
    val dailyVolume: Double = 0.0,
    val dailyCalories: Int = 0,
    val todaysWorkouts: List<WorkoutWithExercises> = emptyList(),

    // Global Stats
    val filteredCalories: Int = 0, // Keeping for compatibility if I missed any reference, but logic uses dailyCalories now. Wait, DailyOverviewSection uses filteredCalories? No, I updated it to use dailyCalories. 
    // Actually, I should check if I missed any references. 
    // I will include filteredCalories but default it to 0 just in case.
    val totalCaloriesJson: List<CaloriePoint> = emptyList(), // Used for graph? I removed graph from Summary?
    // DailyOverviewSection logic was: "Calorie Bar Chart... if (uiState.totalCaloriesJson.isNotEmpty())"
    // I REMOVED the chart from DailyOverviewSection in my previous update (Step 770). 
    // Wait, let me check Step 770 content.
    // DailyOverviewSection lines 198-230 in replacement:
    // It shows StatCards.
    // It shows GlassyCard for Volume.
    // It shows Today's Workouts List.
    // IT DOES NOT SHOW THE CHART.
    // So `totalCaloriesJson` is UNUSED in Summary.
    // But `ProgressSection` (Tab 2) might need it? 
    // User didn't ask for Calorie Chart in Progress, but "Trends" had it?
    // Actually, `GlobalTrendsSection` (Step 770) shows Weekly/Monthly Volume Charts.
    // It does NOT show Calorie Chart.
    // So Calorie Chart is gone?
    // That's fine for "Daily Summary" as per user request. "Daily" usually means simple numbers.
    // I can remove `totalCaloriesJson` if unused. 
    // But to be safe and avoid compilation errors in other files, I'll keep it or strict clean.
    // I'll keep it for safety.
    
    val weeklyVolume: List<VolumePoint> = emptyList(),
    val monthlyVolume: List<VolumePoint> = emptyList(),
    
    // Strength Progression
    val availableExercises: List<String> = emptyList(),
    val selectedExercise: String? = null,
    val exerciseProgress: ExerciseProgress? = null,
    val exerciseHistory: List<ExerciseHistoryPoint> = emptyList(),
    
    val isLoading: Boolean = false,
    val error: String? = null
)

@Immutable
data class CaloriePoint(
    val date: String,
    val calories: Int
)

@Immutable
data class VolumePoint(
    val date: String, // Start of week/month
    val volume: Double
)

@Immutable
data class ExerciseProgress(
    val currentMax: Double,
    val startMax: Double,
    val diffKm: Double,
    val diffPercent: Double
)

@Immutable
data class ExerciseHistoryPoint(
    val date: String,
    val maxWeight: Double,
    val estimated1RM: Double
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val vitaminRepository: VitaminRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()
    
    private var allWorkoutsCache: List<WorkoutWithExercises> = emptyList()
    
    init {
        loadData()
    }
    
    // TimeRange is removed/unused for now as per "Strict Daily Summary" request
    // If needed for Progress tab filters, we can re-add distinct filter later.
    
    fun refresh() {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Load all workouts once
                allWorkoutsCache = workoutRepository.getAllWorkouts().first()
                
                // 1. Calculate Daily Stats (Summary Tab)
                calculateDailyStats(allWorkoutsCache)

                // 2. Calculate Global Stats (Progress Tab)
                calculateGlobalStats(allWorkoutsCache)
                
                // 3. Load Exercises for Dropdown (Progress Tab)
                val exerciseNames = allWorkoutsCache.flatMap { it.exercises }
                    .map { it.exercise.name }
                    .distinct()
                    .take(10)
                
                val defaultExercise = exerciseNames.firstOrNull()

                _uiState.update {
                    it.copy(
                        availableExercises = exerciseNames,
                        selectedExercise = defaultExercise,
                        isLoading = false
                    )
                }
                
                if (defaultExercise != null) {
                    loadExerciseProgression(defaultExercise)
                }
                
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun calculateDailyStats(workouts: List<WorkoutWithExercises>) {
        val today = LocalDate.now()
        val todayStr = today.toString() // YYYY-MM-DD

        // Filter for TODAY
        val todayWorkouts = workouts.filter { 
            it.workout.date.startsWith(todayStr)
        }

        // Daily Calories (Parse from notes)
        val calorieRegex = Regex("Calories: (\\d+)")
        val totalCalories = todayWorkouts.sumOf { workout ->
             val match = calorieRegex.find(workout.workout.notes)
             match?.groupValues?.get(1)?.toIntOrNull() ?: 0
        }

        _uiState.update {
            it.copy(
                dailyWorkoutsCount = todayWorkouts.size,
                dailyVolume = todayWorkouts.sumOf { w -> w.totalVolume },
                dailyCalories = totalCalories,
                todaysWorkouts = todayWorkouts
            )
        }
    }
    
    private fun calculateGlobalStats(workouts: List<WorkoutWithExercises>) {
        // Weekly Volume (Last 12 weeks)
        val weekly = workouts
             .groupBy { 
                 try {
                     if (it.workout.date.length >= 10) {
                         val date = LocalDate.parse(it.workout.date.take(10))
                         val dayOfWeek = date.dayOfWeek.value
                         date.minusDays((dayOfWeek - 1).toLong()).toString()
                     } else "Unknown"
                 } catch (e: Exception) { "Unknown" }
             }
             .filterKeys { it != "Unknown" }
             .mapValues { (_, w) -> w.sumOf { it.totalVolume } }
             .map { VolumePoint(it.key, it.value) }
             .sortedBy { it.date }
             .takeLast(12) 

        // Monthly Volume (Last 12 months)
        val monthly = workouts
             .groupBy { 
                 try {
                     if (it.workout.date.length >= 7) {
                         it.workout.date.substring(0, 7) // YYYY-MM
                     } else "Unknown"
                 } catch (e: Exception) { "Unknown" }
             }
             .filterKeys { it != "Unknown" }
             .mapValues { (_, w) -> w.sumOf { it.totalVolume } }
             .map { VolumePoint(it.key, it.value) }
             .sortedBy { it.date }
             .takeLast(12) 

        _uiState.update { 
            it.copy(
                weeklyVolume = weekly,
                monthlyVolume = monthly
            ) 
        }
    }
    
    fun selectExercise(exerciseName: String) {
        _uiState.update { it.copy(selectedExercise = exerciseName) }
        loadExerciseProgression(exerciseName)
    }
    
    private fun loadExerciseProgression(exerciseName: String) {
        // Full history for the chosen exercise
        val historyPoints = allWorkoutsCache
            .flatMap { workout ->
                workout.exercises
                    .filter { it.exercise.name == exerciseName }
                    .flatMap { it.sets }
                    .mapNotNull { set -> 
                        try {
                           if (workout.workout.date.length >= 10) {
                                val oneRM = set.weight * (1 + set.reps / 30.0)
                                ExerciseHistoryPoint(
                                    date = workout.workout.date, 
                                    maxWeight = set.weight, 
                                    estimated1RM = oneRM
                                )
                           } else null
                        } catch (e: Exception) { null }
                    }
            }
            .groupBy { it.date.take(10) }
            .mapNotNull { (date, points) ->
                 // Safe maxBy
                 points.maxByOrNull { it.maxWeight }
            }
            .sortedBy { it.date }

        if (historyPoints.isNotEmpty()) {
             val first = historyPoints.first()
             val last = historyPoints.last()
             val diff = last.maxWeight - first.maxWeight
             val percent = if (first.maxWeight > 0) (diff / first.maxWeight) * 100 else 0.0
             
             _uiState.update {
                 it.copy(
                     exerciseHistory = historyPoints,
                     exerciseProgress = ExerciseProgress(
                         currentMax = last.maxWeight,
                         startMax = first.maxWeight,
                         diffKm = diff,
                         diffPercent = percent
                     )
                 )
             }
        } else {
             _uiState.update { it.copy(exerciseHistory = emptyList(), exerciseProgress = null) }
        }
    }
}
