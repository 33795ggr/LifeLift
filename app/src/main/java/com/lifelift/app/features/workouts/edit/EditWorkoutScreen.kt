package com.lifelift.app.features.workouts.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity
import com.lifelift.app.features.workouts.ImmutableList
import com.lifelift.app.core.ui.theme.AppColors
import com.lifelift.app.core.ui.utils.rubberBand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutScreen(
    workoutId: Long,
    onNavigateBack: () -> Unit,
    viewModel: EditWorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState() // STATIC SCROLL STATE
    
    LaunchedEffect(workoutId) {
        viewModel.loadWorkout(workoutId)
    }

    var workoutName by remember(uiState.initialName) { mutableStateOf(uiState.initialName) }
    
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
            viewModel.resetSaveState()
        }
    }
    
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopBar(
                workoutName = workoutName,
                isLoading = uiState.isLoading,
                onNavigateBack = onNavigateBack,
                onSave = { viewModel.updateWorkout(workoutName, 45, "") }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading && uiState.addedExercises.items.isEmpty()) {
            LoadingBox(paddingValues)
        } else {
            // THE STATIC "PRE-RENDERED" STACK
            // We use Column + verticalScroll instead of LazyColumn to render EVERYTHING once.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .rubberBand() // RESTORE JELLY EFFECT
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Workout Title Card
                EditWorkoutInputCard(
                    label = "Название тренировки",
                    value = workoutName,
                    placeholder = "напр., День груди",
                    onValueChange = { workoutName = it }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Упражнения",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // FULL RENDER OF ALL EXERCISES
                uiState.addedExercises.items.forEachIndexed { exIdx, exercise ->
                    key(exercise.id) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                                .background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp))
                                .padding(bottom = 8.dp)
                        ) {
                            ExerciseHeader(
                                index = exIdx,
                                exerciseName = exercise.name,
                                availableExercises = uiState.availableExercises,
                                onRemove = { viewModel.removeExercise(exIdx) },
                                onNameChange = { viewModel.updateExerciseName(exIdx, it) }
                            )

                            exercise.sets.items.forEachIndexed { sIdx, set ->
                                key(set.id) {
                                    EditSetRow(
                                        index = sIdx,
                                        reps = set.reps,
                                        weight = set.weight,
                                        onUpdateReps = { viewModel.updateSet(exIdx, sIdx, "reps", it) },
                                        onUpdateWeight = { viewModel.updateSet(exIdx, sIdx, "weight", it) },
                                        onRemove = { viewModel.removeSetFromExercise(exIdx, sIdx) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            
                            ExerciseFooter(
                                onAddSet = { viewModel.addSetToExercise(exIdx) }
                            )
                        }
                    }
                }

                // Add Exercise Button
                OutlinedButton(
                    onClick = { viewModel.addEmptyExercise() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00BFFF)),
                    border = BorderStroke(1.dp, Color(0xFF00BFFF))
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Добавить упражнение", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseHeader(
    index: Int,
    exerciseName: String,
    availableExercises: ImmutableList<ExerciseRefEntity>,
    onRemove: () -> Unit,
    onNameChange: (String) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color.White
        ) {
            // LazyColumn is fine INSIDE the sheet since it's a separate window layer
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .padding(bottom = 32.dp)
            ) {
                item {
                    Text(
                        text = "Выберите упражнение",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                
                items(availableExercises.items.size) { i ->
                    val exercise = availableExercises.items[i]
                    TextButton(
                        onClick = {
                            onNameChange(exercise.name)
                            showSheet = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = exercise.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFF333333))
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Упражнение ${index + 1}", fontSize = 14.sp, color = Color(0xFFB3B3B3))
            IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, null, tint = Color(0xFF666666), modifier = Modifier.size(18.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Surface(
            onClick = { showSheet = true },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF2A2A2A),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (exerciseName.isBlank()) "Выберите упражнение" else exerciseName,
                    color = if (exerciseName.isBlank()) Color(0xFF666666) else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(Icons.Default.ArrowDropDown, null, tint = Color(0xFF666666))
            }
        }
    }
}

@Composable
private fun ExerciseFooter(
    onAddSet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedButton(
            onClick = onAddSet,
            modifier = Modifier.fillMaxWidth().height(40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00BFFF))
        ) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Добавить подход", fontSize = 14.sp)
        }
    }
}

@Composable
private fun TopBar(
    workoutName: String,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.size(32.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Text(text = "Редактировать\nтренировку", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White, lineHeight = 26.sp)
        }
        TextButton(onClick = onSave, enabled = !isLoading) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF00BFFF), strokeWidth = 2.dp)
            } else {
                Text(text = "Сохранить", color = Color(0xFF00BFFF), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun EditWorkoutInputCard(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = label, fontSize = 14.sp, color = Color(0xFFB3B3B3))
            androidx.compose.material3.TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = placeholder, color = Color(0xFF666666)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF00BFFF),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
private fun LoadingBox(paddingValues: PaddingValues) {
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF00BFFF))
    }
}
