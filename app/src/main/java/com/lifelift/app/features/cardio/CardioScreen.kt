package com.lifelift.app.features.cardio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelift.app.R
import com.lifelift.app.core.data.local.entity.WorkoutEntity
import com.lifelift.app.core.data.preferences.UserPreferencesManager
import com.lifelift.app.core.data.repository.WorkoutRepository
import com.lifelift.app.core.util.DateUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt
import com.lifelift.app.core.ui.components.GlassyCard
import com.lifelift.app.core.ui.utils.rubberBand
import com.lifelift.app.core.ui.theme.AppColors
import androidx.compose.ui.graphics.Brush
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOn

enum class CardioType(val displayNameRes: Int, val met: Double) {
    RUNNING(R.string.activity_run, 9.8), // Running 6 mph (10 min/mile)
    CYCLING(R.string.activity_cycle, 7.5), // Bicycling, general
    WALKING(R.string.activity_walk, 3.8), // Walking 3.5 mph
    SWIMMING(R.string.activity_swim, 8.0), // Swimming laps, freestyle, slow
    HIIT(R.string.activity_hiit, 8.0) // HIIT
}

@HiltViewModel
class CardioViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardioUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesManager.weight.collect { weight ->
                _uiState.update { 
                    it.copy(
                        bodyWeight = weight,
                        bodyWeightInput = if (it.bodyWeightInput == "70" && weight != 70.0) weight.toString().removeSuffix(".0") else it.bodyWeightInput
                    ) 
                }
                calculateCalories()
            }
        }
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            workoutRepository.getWorkoutsByType(com.lifelift.app.core.data.local.entity.WorkoutType.CARDIO)
                .map { workoutsWithExercises ->
                    workoutsWithExercises.map { it.workout }.map { workout ->
                        val dateObj = try { LocalDateTime.parse(workout.date) } catch(e: Exception) { null }
                        val dateStr = dateObj?.format(DateTimeFormatter.ofPattern("MMM d")) ?: workout.date.take(10)
                        
                        CardioUiModel(
                            id = workout.id,
                            name = workout.name,
                            dateDisplay = dateStr,
                            durationMinutes = workout.durationMinutes,
                            notes = workout.notes,
                            rawData = workout
                        )
                    }
                }
                .flowOn(Dispatchers.Default)
                .collect { uiHistory ->
                    _uiState.update { it.copy(history = uiHistory) }
                }
        }
    }

    fun deleteSession(workout: WorkoutEntity) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workout)
        }
    }

    fun prepareEdit(workout: WorkoutEntity) {
        _uiState.update { it.copy(showEditDialog = true, editingSession = workout) }
    }

    fun dismissEdit() {
        _uiState.update { it.copy(showEditDialog = false, editingSession = null) }
    }

    fun updateSession(workout: WorkoutEntity, newDuration: Int, newCalories: Int) {
         viewModelScope.launch {
             val updated = workout.copy(
                 durationMinutes = newDuration,
                 notes = "Calories: $newCalories kcal"
             )
             workoutRepository.updateWorkout(updated)
             dismissEdit()
         }
    }

    fun updateDuration(duration: String) {
        _uiState.update { 
            it.copy(duration = duration) 
        }
        calculateCalories()
    }

    fun selectActivity(type: CardioType) {
        _uiState.update { 
            it.copy(selectedActivity = type) 
        }
        calculateCalories()
    }
    
    fun updateWeight(weightInput: String) {
         _uiState.update { it.copy(bodyWeightInput = weightInput) }
         
         val weightVal = weightInput.toDoubleOrNull()
         if (weightVal != null) {
             _uiState.update { it.copy(bodyWeight = weightVal) }
             calculateCalories()
             
             viewModelScope.launch {
                 userPreferencesManager.setWeight(weightVal)
             }
         }
    }

    private fun calculateCalories() {
        val state = _uiState.value
        val durationMin = state.duration.toIntOrNull() ?: 0
        val weightKg = state.bodyWeight
        val met = state.selectedActivity.met

        val calories = (met * weightKg * durationMin) / 60.0
        
        _uiState.value = state.copy(caloriesBurned = calories.roundToInt())
    }

    fun logSession() {
        val state = _uiState.value
        if (state.duration.isBlank()) return

        viewModelScope.launch {
            try {
                val workout = WorkoutEntity(
                    name = state.selectedActivity.name, 
                    date = DateUtils.getCurrentDateTime(),
                    durationMinutes = state.duration.toIntOrNull() ?: 0,
                    notes = "Calories: ${state.caloriesBurned} kcal",
                    type = com.lifelift.app.core.data.local.entity.WorkoutType.CARDIO
                )
                workoutRepository.insertWorkout(workout)
                _uiState.value = state.copy(isSaved = true)
            } catch (e: Exception) {
            }
        }
    }
    
    fun resetSaveState() {
        _uiState.value = _uiState.value.copy(isSaved = false, duration = "", caloriesBurned = 0)
    }
}

@Immutable
data class CardioUiModel(
    val id: Long,
    val name: String,
    val dateDisplay: String,
    val durationMinutes: Int,
    val notes: String,
    val rawData: WorkoutEntity
)

@Immutable
data class CardioUiState(
    val selectedActivity: CardioType = CardioType.RUNNING,
    val duration: String = "",
    val bodyWeightInput: String = "70",
    val bodyWeight: Double = 70.0,
    val caloriesBurned: Int = 0,
    val isSaved: Boolean = false,
    val history: List<CardioUiModel> = emptyList(),
    val showEditDialog: Boolean = false,
    val editingSession: WorkoutEntity? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardioScreen(
    onNavigateBack: () -> Unit,
    viewModel: CardioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val savedMessage = stringResource(R.string.msg_cardio_logged)

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            snackbarHostState.showSnackbar(savedMessage)
            viewModel.resetSaveState()
        }
    }
    
    if (uiState.showEditDialog && uiState.editingSession != null) {
        EditSessionDialog(
            workout = uiState.editingSession!!,
            userWeight = uiState.bodyWeight,
            onDismiss = { viewModel.dismissEdit() },
            onSave = { duration, calories -> viewModel.updateSession(uiState.editingSession!!, duration, calories) }
        )
    }

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AppColors.CardioRose.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                Column {
                    TopAppBar(
                        title = { Text(stringResource(R.string.cardio_title), fontWeight = FontWeight.Bold, fontSize = 28.sp) },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                    
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = AppColors.CardioRose,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = AppColors.CardioRose
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text(stringResource(R.string.label_new_session)) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text(stringResource(R.string.label_history)) }
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            if (selectedTab == 0) {
                NewSessionSection(uiState, viewModel)
            } else {
                HistorySection(uiState, viewModel)
            }
        }
    }
}

@Composable
fun NewSessionSection(
    uiState: CardioUiState,
    viewModel: CardioViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .rubberBand(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.cardio_subtitle),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.bodyWeightInput,
                    onValueChange = { viewModel.updateWeight(it) },
                    label = { Text(stringResource(R.string.label_body_weight)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                Text(
                    text = stringResource(R.string.label_activity_type),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            items(CardioType.values()) { type ->
                    val isSelected = uiState.selectedActivity == type
                    GlassyCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { viewModel.selectActivity(type) },
                        backgroundColor = if (isSelected) 
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f) 
                        else 
                            null,
                        accentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(type.displayNameRes),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSelected) {
                                Text(
                                    text = "MET: ${type.met}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                }
            }
        }

            item {
                OutlinedTextField(
                    value = uiState.duration,
                    onValueChange = { viewModel.updateDuration(it) },
                    label = { Text(stringResource(R.string.label_duration_minutes)) },
                    placeholder = { Text("30") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE57373).copy(alpha = 0.1f) // Light Red
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE57373))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.label_calories_burned),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFE57373)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${uiState.caloriesBurned} kcal",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE57373)
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.logSession() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = uiState.duration.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.btn_log_cardio),
                        fontSize = 18.sp
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun HistorySection(
    uiState: CardioUiState,
    viewModel: CardioViewModel
) {
    if (uiState.history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No history yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            @OptIn(ExperimentalFoundationApi::class)
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .rubberBand(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.history,
                        key = { it.id },
                        contentType = { "cardio_history" }
                    ) { workout ->
                        GlassyCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = workout.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = workout.dateDisplay,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${workout.durationMinutes} min • ${workout.notes}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Row {
                                    IconButton(onClick = { viewModel.prepareEdit(workout.rawData) }) {
                                        Icon(Icons.Default.Edit, "Edit", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { viewModel.deleteSession(workout.rawData) }) {
                                        Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditSessionDialog(
    workout: WorkoutEntity,
    userWeight: Double,
    onDismiss: () -> Unit,
    onSave: (Int, Int) -> Unit
) {
    var duration by remember { mutableStateOf(workout.durationMinutes.toString()) }
    var calories by remember { 
        val cals = workout.notes.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
        mutableStateOf(cals.toString()) 
    }

    val type = remember(workout.name) {
        try {
            CardioType.valueOf(workout.name)
        } catch (e: Exception) {
            CardioType.RUNNING
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.title_edit_session)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = duration,
                    onValueChange = { newVal ->
                        duration = newVal
                        val d = newVal.toIntOrNull()
                        if (d != null && userWeight > 0) {
                            val c = (type.met * userWeight * d) / 60.0
                            calories = c.roundToInt().toString()
                        }
                    },
                    label = { Text(stringResource(R.string.label_duration_minutes)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text(stringResource(R.string.label_calories)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(duration.toIntOrNull() ?: 0, calories.toIntOrNull() ?: 0)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

