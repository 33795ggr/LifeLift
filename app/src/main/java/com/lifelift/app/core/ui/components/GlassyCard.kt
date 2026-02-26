package com.lifelift.app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.foundation.isSystemInDarkTheme

/**
 * Premium Glass Card with Apple-inspired glassmorphism effect.
 * Optimized using standard modifiers for better performance.
 */
@Composable
fun GlassyCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 20.dp,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }
    
    // SOLID BACKGROUND ONLY for 120 FPS
    val glassColor = remember(isDark, backgroundColor) {
        backgroundColor ?: if (isDark) {
            Color(0xFF1E1E1E)
        } else {
            Color.White
        }
    }
    
    Box(
        modifier = modifier
            .graphicsLayer {
                this.shape = shape
                this.clip = true
            }
            .background(glassColor)
            .border(1.dp, accentColor.copy(alpha = 0.2f), shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(20.dp),
        content = content
    )
}

/**
 * Premium Feature Card for main navigation
 * Optimized version: merged backgrounds and simplified brushes.
 */
@Composable
fun FeatureGlassCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable BoxScope.() -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val shape = remember { RoundedCornerShape(24.dp) }
    
    // SIMPLIFIED SOLID-LIKE GRADIENT
    val bgBrush = remember(isDarkTheme, accentColor) {
        val baseColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFFCFCFC)
        val bottomColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color(0xFFF2F2F2)
        Brush.verticalGradient(
            colors = listOf(baseColor, bottomColor)
        )
    }
    
    Box(
        modifier = modifier
            .graphicsLayer {
                this.shape = shape
                this.clip = true
            }
            .background(bgBrush)
            .border(1.2.dp, accentColor.copy(alpha = 0.25f), shape)
            .clickable(onClick = onClick)
            .padding(24.dp),
        content = content
    )
}
