package com.lifelift.app.features.workouts.create

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity
import com.lifelift.app.features.workouts.ExerciseUiData
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import com.lifelift.app.core.ui.utils.rubberBand
import com.lifelift.app.core.ui.theme.AppColors
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CreateWorkoutScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateWorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Setup state
    var workoutName by remember { mutableStateOf("") }
    
    // Effect to handle navigation after saving
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
            viewModel.resetSaveState()
        }
    }
    
    // Handle Back Press
    BackHandler {
        if (uiState.step == CreationStep.EXERCISES) {
            viewModel.goBackToSetup()
        } else {
            onNavigateBack()
        }
    }
    
    Scaffold(
        containerColor = Color.Black
    ) { paddingValues ->
        // Use IME padding to push content up
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding()
        ) {
            
            AnimatedContent(
                targetState = uiState.step,
                transitionSpec = {
                    if (targetState == CreationStep.EXERCISES) {
                        slideInHorizontally { width -> width } + fadeIn() with
                        slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() with
                        slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "WizardTransition"
            ) { step ->
                when (step) {
                    CreationStep.SETUP -> {
                        SetupStepScreen(
                            workoutName = workoutName,
                            onNameChange = { workoutName = it },
                            onStart = { viewModel.proceedToExercises() },
                            onBack = onNavigateBack
                        )
                    }
                    CreationStep.EXERCISES -> {
                        ExerciseWizardStepScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            workoutName = workoutName,
                            onBack = { viewModel.goBackToSetup() },
                            onFinish = { viewModel.saveWorkout(workoutName, 60, "") }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupStepScreen(
    workoutName: String,
    onNameChange: (String) -> Unit,
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
            .rubberBand() // RESTORE JELLY
            .verticalScroll(scrollState)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                stringResource(com.lifelift.app.R.string.wizard_step_setup),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        
        // Input Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.SurfaceDark, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column {
                Text(
                    stringResource(com.lifelift.app.R.string.label_workout_name),
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = workoutName,
                    onValueChange = onNameChange,
                    placeholder = { Text(stringResource(com.lifelift.app.R.string.placeholder_workout_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00BFFF),
                        unfocusedBorderColor = Color.Gray,
                        focusedContainerColor = AppColors.SurfaceDarker,
                        unfocusedContainerColor = AppColors.SurfaceDarker
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(stringResource(com.lifelift.app.R.string.wizard_btn_start), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExerciseWizardStepScreen(
    uiState: CreateWorkoutUiState,
    viewModel: CreateWorkoutViewModel,
    workoutName: String,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    val currentExercise = uiState.addedExercises.items.getOrNull(uiState.currentExerciseIndex) ?: return
    var showCreateExerciseDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(uiState.addedExercises.size) { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == uiState.currentExerciseIndex) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (i == uiState.currentExerciseIndex) Color(0xFF00BFFF) else Color(0xFF444444))
                    )
                }
            }
            
            TextButton(onClick = onFinish) {
                 if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF00BFFF))
                 } else {
                    Text(stringResource(com.lifelift.app.R.string.wizard_btn_finish), color = Color(0xFF00BFFF), fontWeight = FontWeight.Bold)
                 }
            }
        }
        
        Text(
            text = stringResource(com.lifelift.app.R.string.wizard_exercise_counter, uiState.currentExerciseIndex + 1, uiState.addedExercises.size),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
        )

        // STATIC CONTAINER
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E), RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
        ) {
            AnimatedContent(
                targetState = uiState.currentExerciseIndex,
                transitionSpec = {
                     if (targetState > initialState) {
                         // Next
                         slideInHorizontally { width -> width } + fadeIn() with 
                         slideOutHorizontally { width -> -width } + fadeOut()
                     } else {
                         // Prev
                         slideInHorizontally { width -> -width } + fadeIn() with 
                         slideOutHorizontally { width -> width } + fadeOut()
                     }
                },
                label = "ExerciseSlide",
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) { pageIndex ->
                val exerciseData = uiState.addedExercises.items.getOrNull(pageIndex)
                if (exerciseData != null) {
                    ExerciseEditor(
                        exercise = exerciseData,
                        index = pageIndex,
                        availableExercises = uiState.availableExercises,
                        onUpdateName = { name -> viewModel.updateExerciseName(pageIndex, name) },
                        onAddSet = { viewModel.addSet(pageIndex) },
                        onRemoveSet = { sIdx -> viewModel.removeSet(pageIndex, sIdx) },
                        onUpdateSet = { sIdx, r, w -> viewModel.updateSet(pageIndex, sIdx, r, w) },
                        onCreateNewExercise = { showCreateExerciseDialog = true }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (uiState.currentExerciseIndex > 0) {
                 OutlinedButton(
                    onClick = { viewModel.previousExercise() },
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                     border = BorderStroke(1.dp, Color.Gray),
                     shape = RoundedCornerShape(12.dp)
                ) {
                     Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                     Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(com.lifelift.app.R.string.wizard_btn_prev))
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            Button(
                onClick = { viewModel.nextExercise() },
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                 shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(com.lifelift.app.R.string.wizard_btn_next_exercise))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
    
    if (showCreateExerciseDialog) {
        NewExerciseDialog(
            onDismiss = { showCreateExerciseDialog = false },
            onConfirm = { name, category ->
                viewModel.createAndSelectExercise(name, category, 1, 10, 0.0) 
                showCreateExerciseDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEditor(
    exercise: ExerciseUiData,
    index: Int,
    availableExercises: List<ExerciseRefEntity>,
    onUpdateName: (String) -> Unit,
    onAddSet: () -> Unit,
    onRemoveSet: (Int) -> Unit,
    onUpdateSet: (Int, Int, Double) -> Unit,
    onCreateNewExercise: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scrollState = rememberScrollState() // STATIC SCROLL

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color.White
        ) {
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .padding(bottom = 32.dp)
            ) {
                item {
                    Text(
                        text = stringResource(com.lifelift.app.R.string.wizard_select_exercise),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                
                item {
                    TextButton(
                        onClick = {
                            onCreateNewExercise()
                            showSheet = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = stringResource(com.lifelift.app.R.string.wizard_create_new_exercise),
                            color = Color(0xFF00BFFF),
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                items(availableExercises.size) { i ->
                    val ex = availableExercises[i]
                    TextButton(
                        onClick = {
                            onUpdateName(ex.name)
                            showSheet = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = ex.name,
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

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(com.lifelift.app.R.string.label_exercise_name),
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Surface(
            onClick = { showSheet = true },
            modifier = Modifier.fillMaxWidth(),
            color = AppColors.SurfaceDarker,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (exercise.name.isBlank()) stringResource(com.lifelift.app.R.string.wizard_select_exercise) else exercise.name,
                    color = if (exercise.name.isBlank()) Color.Gray else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(Icons.Default.ArrowDropDown, null, tint = Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            stringResource(com.lifelift.app.R.string.wizard_header_sets),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // STATIC SETS SCROLL
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .rubberBand() // RESTORE JELLY
                .verticalScroll(scrollState)
        ) {
             // Header Row
             Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(stringResource(com.lifelift.app.R.string.wizard_col_num), color=Color.Gray, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                Text(stringResource(com.lifelift.app.R.string.wizard_col_reps), color=Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(stringResource(com.lifelift.app.R.string.wizard_col_kg), color=Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.width(40.dp))
            }
            
            exercise.sets.items.forEachIndexed { i, setItem ->
                key(setItem.id) {
                    val onSetUpdate = remember(i, onUpdateSet) {
                        { reps: Int, weight: Double -> onUpdateSet(i, reps, weight) }
                    }

                    SetRowLarge(
                        index = i,
                        reps = setItem.reps,
                        weight = setItem.weight,
                        onUpdate = onSetUpdate,
                        onRemove = { onRemoveSet(i) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            
            Button(
                onClick = onAddSet,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.SurfaceDarker),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF00BFFF))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(com.lifelift.app.R.string.wizard_btn_add_set), color = Color(0xFF00BFFF))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SetRowLarge(
    index: Int,
    reps: Int,
    weight: Double,
    onUpdate: (Int, Double) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${index + 1}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
        
        BigInput(
            value = if (reps > 0) reps.toString() else "",
            onValueChange = { 
                val r = it.filter { c -> c.isDigit() }.toIntOrNull() ?: 0
                onUpdate(r, weight)
            },
            modifier = Modifier.weight(1f)
        )
        
        BigInput(
             value = if (weight > 0) {
                if (weight % 1.0 == 0.0) weight.toInt().toString() else weight.toString()
            } else "",
            onValueChange = { 
                val w = it.filter { c -> c.isDigit() || c == '.' }.toDoubleOrNull() ?: 0.0
                onUpdate(reps, w)
            },
            modifier = Modifier.weight(1f)
        )
        
        IconButton(onClick = onRemove, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Default.Close, contentDescription = stringResource(com.lifelift.app.R.string.wizard_desc_delete_set), tint = Color.Gray)
        }
    }
}

@Composable
fun BigInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    var text by remember(value) { mutableStateOf(value) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .height(56.dp)
            .background(AppColors.SurfaceDarker, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.text.BasicTextField(
            value = text,
            onValueChange = { 
                val filtered = it.filter { c -> c.isDigit() || c == '.' }
                text = filtered
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onDone = {
                    onValueChange(text)
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && text != value) {
                        onValueChange(text)
                    }
                },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Color(0xFF00BFFF)),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    if (text.isEmpty()) {
                        Text("0", color = Color(0xFF666666), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun NewExerciseDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(com.lifelift.app.R.string.dialog_new_exercise_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(com.lifelift.app.R.string.label_exercise_name)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(stringResource(com.lifelift.app.R.string.label_category)) },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name, category) },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(com.lifelift.app.R.string.btn_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(com.lifelift.app.R.string.btn_cancel))
            }
        }
    )
}
