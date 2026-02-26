package com.lifelift.app.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lifelift.app.core.data.preferences.ThemeMode

// Premium Shape System
object Shapes {
    val ExtraSmall = RoundedCornerShape(12.dp)
    val Small = RoundedCornerShape(16.dp)
    val Medium = RoundedCornerShape(20.dp)
    val Large = RoundedCornerShape(24.dp)
    val ExtraLarge = RoundedCornerShape(32.dp)
}

// Dark Theme (Standard)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00D9FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0099CC),
    onPrimaryContainer = Color.White,
    
    secondary = Color(0xFF6EFFC4),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF4DCC9A),
    onSecondaryContainer = Color.White,
    
    tertiary = Color(0xFFBB86FC),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF9965D4),
    onTertiaryContainer = Color.White,
    
    background = Color(0xFF121212),
    onBackground = Color.White,
    
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB3B3B3),
    
    error = Color(0xFFFF5252),
    onError = Color.White,
    
    outline = Color(0xFF3A3A3A),
    outlineVariant = Color(0xFF2A2A2A)
)

// AMOLED Theme (Pure Black)
private val AmoledColorScheme = darkColorScheme(
    primary = Color(0xFF00D9FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0099CC),
    onPrimaryContainer = Color.White,
    
    secondary = Color(0xFF6EFFC4),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF4DCC9A),
    onSecondaryContainer = Color.White,
    
    tertiary = Color(0xFFBB86FC),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF9965D4),
    onTertiaryContainer = Color.White,
    
    background = Color(0xFF000000), // Pure black
    onBackground = Color.White,
    
    surface = Color(0xFF0A0A0A), // Near black
    onSurface = Color.White,
    surfaceVariant = Color(0xFF151515),
    onSurfaceVariant = Color(0xFFB3B3B3),
    
    error = Color(0xFFFF5252),
    onError = Color.White,
    
    outline = Color(0xFF222222),
    outlineVariant = Color(0xFF1A1A1A)
)

// Light Theme
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0055CC),
    onPrimaryContainer = Color.White,
    
    secondary = Color(0xFF30D158),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF28A745),
    onSecondaryContainer = Color.White,
    
    tertiary = Color(0xFF6200EE),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF4B00B5),
    onTertiaryContainer = Color.White,
    
    background = Color(0xFFF5F5F7),
    onBackground = Color.Black,
    
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = Color(0xFF666666),
    
    error = Color(0xFFFF3B30),
    onError = Color.White,
    
    outline = Color(0xFFE5E5EA),
    outlineVariant = Color(0xFFF0F0F0)
)

@Composable
fun LifeLiftTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemDarkTheme = isSystemInDarkTheme()
    
    val colorScheme = when (themeMode) {
        ThemeMode.LIGHT -> LightColorScheme
        ThemeMode.DARK -> DarkColorScheme
        ThemeMode.AMOLED -> AmoledColorScheme
        ThemeMode.SYSTEM -> if (systemDarkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
