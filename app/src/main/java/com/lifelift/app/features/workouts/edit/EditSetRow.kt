package com.lifelift.app.features.workouts.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditSetRow(
    index: Int,
    reps: String,
    weight: String,
    onUpdateReps: (String) -> Unit,
    onUpdateWeight: (String) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pure Box-based implementation for extreme speed
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Set Number Badge
        Box(
            modifier = Modifier
                .width(56.dp)
                .height(48.dp)
                .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Подход",
                    fontSize = 10.sp,
                    color = Color(0xFF888888),
                    lineHeight = 10.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${index + 1}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 16.sp
                )
            }
        }
        
        // Weight Input
        StaticInput(
            label = "кг",
            value = weight,
            onCommit = onUpdateWeight,
            modifier = Modifier.weight(1f)
        )
        
        // Reps Input
        StaticInput(
            label = "повт",
            value = reps,
            onCommit = onUpdateReps,
            modifier = Modifier.weight(1f)
        )
        
        // Remove Button
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Удалить сет",
                tint = Color(0xFF666666),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun StaticInput(
    label: String,
    value: String,
    onCommit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember(value) { mutableStateOf(value) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .height(40.dp)
            .background(Color(0xFF2A2A2A), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.text.BasicTextField(
                value = text,
                onValueChange = { newText ->
                    val filtered = newText.filter { c -> c.isDigit() || c == '.' }
                    text = filtered
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onCommit(text)
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused && text != value) {
                            onCommit(text)
                        }
                    },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                cursorBrush = SolidColor(Color(0xFF00BFFF)),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "0",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF666666)
                            )
                        }
                        innerTextField()
                    }
                }
            )
            
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFFB3B3B3)
            )
        }
    }
}
