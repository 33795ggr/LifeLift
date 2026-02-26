package com.lifelift.app.features.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelift.app.R
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity
import com.lifelift.app.core.ui.components.GlassyCard

import com.lifelift.app.core.ui.utils.rubberBand
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    viewModel: ExercisesViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    var showEditSheet by remember { mutableStateOf(false) }
    var exerciseToEdit by remember { mutableStateOf<ExerciseRefEntity?>(null) } // Null means new exercise
    var showDeleteDialog by remember { mutableStateOf(false) }
    var exerciseToDelete by remember { mutableStateOf<ExerciseRefEntity?>(null) }
    val haptics = LocalHapticFeedback.current

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).clipToBounds()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            @OptIn(ExperimentalFoundationApi::class)
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                LazyColumn(
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .rubberBand()
                ) {
                    if (uiState.exercises.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(com.lifelift.app.R.string.msg_no_exercises_found),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        items(
                            items = uiState.exercises,
                            key = { it.id },
                            contentType = { "exercise" }
                        ) { exercise ->
                            ExerciseItem(
                                exercise = exercise,
                                onEdit = {
                                    exerciseToEdit = exercise.rawData
                                    showEditSheet = true
                                },
                                onDelete = {
                                    exerciseToDelete = exercise.rawData
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                exerciseToEdit = null
                showEditSheet = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Exercise")
        }
    }

    if (showEditSheet) {
        ExerciseEditorSheet(
            exercise = exerciseToEdit,
            onDismiss = { showEditSheet = false },
            onConfirm = { name, category ->
                if (exerciseToEdit == null) {
                    viewModel.addExercise(name, category)
                } else {
                    viewModel.updateExercise(exerciseToEdit!!.copy(name = name, category = category))
                }
                showEditSheet = false
            }
        )
    }
    if (showDeleteDialog && exerciseToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(com.lifelift.app.R.string.dialog_delete_exercise_title)) },
            text = { Text(stringResource(com.lifelift.app.R.string.dialog_delete_exercise_message, exerciseToDelete?.name ?: "")) },
            confirmButton = {
                Button(
                    onClick = {
                        exerciseToDelete?.let { viewModel.deleteExercise(it) }
                        showDeleteDialog = false
                        exerciseToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.btn_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            }
        )
    }
}

@Composable
fun ExerciseItem(
    exercise: ExerciseUiModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    GlassyCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = exercise.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEditorSheet(
    exercise: ExerciseRefEntity?,
    onDismiss: () -> Unit,
    onConfirm: (name: String, category: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var name by remember { mutableStateOf(exercise?.name ?: "") }
    var category by remember { mutableStateOf(exercise?.category ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
             Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
                    .width(48.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                if (exercise == null) stringResource(com.lifelift.app.R.string.title_new_exercise) else stringResource(com.lifelift.app.R.string.title_edit_exercise),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(com.lifelift.app.R.string.label_exercise_name)) },
                        placeholder = { Text(stringResource(com.lifelift.app.R.string.placeholder_exercise_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        enabled = true
                    )
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text(stringResource(com.lifelift.app.R.string.label_category)) },
                        placeholder = { Text(stringResource(com.lifelift.app.R.string.placeholder_category)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }

            Button(
                onClick = { onConfirm(name, category) },
                enabled = name.isNotBlank() && category.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (exercise == null) stringResource(com.lifelift.app.R.string.btn_add) else stringResource(com.lifelift.app.R.string.btn_save), fontWeight = FontWeight.SemiBold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
