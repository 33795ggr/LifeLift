package com.lifelift.app.features.vitamins

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Premium Color Palette (shared with VitaminsScreen)
private val VitaminPurple = Color(0xFFAF52DE)
private val HealthGreen = Color(0xFF34C759)
private val CardDark = Color(0xFF1C1C1E)
private val CardDarker = Color(0xFF2C2C2E)
private val TextGray = Color(0xFF8E8E93)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVitaminSheet(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Boolean, Boolean) -> Unit,
    accentColor: Color
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // State
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("09:00") }

    // Reminder Mode: 0 = Off, 1 = App Notification, 2 = System Alarm
    var reminderMode by remember { mutableIntStateOf(1) }

    // Time Picker State
    var showTimePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF0D0D0D),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            // Premium Gradient Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                VitaminPurple.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CardDark)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Новый витамин",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Добавьте в расписание",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGray
                        )
                    }

                    TextButton(
                        onClick = {
                            val useAlarm = reminderMode == 2
                            val useNotification = reminderMode == 1
                            onConfirm(name, dosage, time, "", useAlarm, useNotification)
                        },
                        enabled = name.isNotBlank() && dosage.isNotBlank()
                    ) {
                        Text(
                            "Готово",
                            color = if (name.isNotBlank() && dosage.isNotBlank()) HealthGreen else TextGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Name Input
                PremiumTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Название",
                    placeholder = "Витамин D3",
                    icon = Icons.Default.Star
                )

                // Dosage Input
                PremiumTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = "Дозировка",
                    placeholder = "2000 МЕ",
                    icon = Icons.Default.Info
                )

                // Time Selector
                PremiumTimeSelector(
                    time = time,
                    onClick = { showTimePicker = true }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Reminder Type Selector
                Text(
                    "Напоминание",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextGray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardDark),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    ReminderOption(
                        title = "Выкл",
                        isSelected = reminderMode == 0,
                        onClick = { reminderMode = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    ReminderOption(
                        title = "Push",
                        isSelected = reminderMode == 1,
                        onClick = { reminderMode = 1 },
                        modifier = Modifier.weight(1f)
                    )
                    ReminderOption(
                        title = "Будильник",
                        isSelected = reminderMode == 2,
                        onClick = { reminderMode = 2 },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Save Button
                Button(
                    onClick = {
                        val useAlarm = reminderMode == 2
                        val useNotification = reminderMode == 1
                        onConfirm(name, dosage, time, "", useAlarm, useNotification)
                    },
                    enabled = name.isNotBlank() && dosage.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VitaminPurple,
                        disabledContainerColor = CardDarker
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Добавить витамин",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            initialTime = time,
            onDismiss = { showTimePicker = false },
            onConfirm = { newTime ->
                time = newTime
                showTimePicker = false
            }
        )
    }
}

@Composable
private fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = TextGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardDark)
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = VitaminPurple,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            placeholder,
                            color = TextGray,
                            fontSize = 16.sp
                        )
                    }

                    androidx.compose.foundation.text.BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumTimeSelector(
    time: String,
    onClick: () -> Unit
) {
    Column {
        Text(
            "Время приёма",
            style = MaterialTheme.typography.labelLarge,
            color = TextGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardDark)
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        tint = VitaminPurple,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        time,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Изменить",
                    tint = TextGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ReminderOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected)
                    Brush.linearGradient(listOf(VitaminPurple, VitaminPurple.copy(alpha = 0.8f)))
                else
                    Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            color = if (isSelected) Color.White else TextGray,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialTime: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val parts = initialTime.split(":")
    val pickerState = rememberTimePickerState(
        initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 9,
        initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0,
        is24Hour = true
    )

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardDark)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Выберите время",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                TimeInput(
                    state = pickerState,
                    colors = TimePickerDefaults.colors(
                        timeSelectorSelectedContainerColor = VitaminPurple.copy(alpha = 0.2f),
                        timeSelectorUnselectedContainerColor = CardDarker,
                        timeSelectorSelectedContentColor = VitaminPurple,
                        timeSelectorUnselectedContentColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Quick Presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("08:00", "12:00", "20:00").forEach { preset ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CardDarker)
                                .clickable { onConfirm(preset) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                preset,
                                color = TextGray,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена", color = TextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val hour = pickerState.hour.toString().padStart(2, '0')
                            val minute = pickerState.minute.toString().padStart(2, '0')
                            onConfirm("$hour:$minute")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VitaminPurple),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Сохранить", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
