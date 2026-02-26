package com.lifelift.app.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Premium Color Palette for LifeLift App
 * Use these colors consistently across all screens for a cohesive look.
 */
object AppColors {
    // Primary Feature Colors (matching home page icons)
    val WorkoutCyan = Color(0xFF00BCD4)         // Soft cyan for workouts
    val WorkoutCyanLight = Color(0xFF4DD0E1)
    
    val CardioRose = Color(0xFFFF6B6B)           // Warm rose for cardio
    val CardioRoseLight = Color(0xFFFF8E8E)
    
    val VitaminPurple = Color(0xFFAF52DE)        // Rich purple for vitamins
    val VitaminPurpleLight = Color(0xFFC782F0)
    
    val AnalyticsIndigo = Color(0xFF5E5CE6)      // Deep indigo for analytics
    val AnalyticsIndigoLight = Color(0xFF8A87F2)
    
    // Accent Colors
    val SuccessGreen = Color(0xFF34C759)
    val WarningOrange = Color(0xFFFF9500)
    val ErrorRed = Color(0xFFFF3B30)
    
    // Neutral Colors
    val CardDark = Color(0xFF1C1C1E)
    val CardDarker = Color(0xFF2C2C2E)
    val TextGray = Color(0xFF8E8E93)
    val TextLightGray = Color(0xFFAEAEB2)
    
    // Solid background alternatives for high-performance (Zero-Alpha)
    val SurfaceDark = Color(0xFF1E1E1E)
    val SurfaceDarker = Color(0xFF111111)
    val InputDark = Color(0xFF2A2A2A)
    
    // Low-cost Glow colors (Solid approximations of <10% Alpha)
    val WorkoutGlow = Color(0xFF001A1D)
    val CardioGlowBuffer = Color(0xFF1A0A0A)
    val VitaminGlowBuffer = Color(0xFF0F0614)
    val AnalyticsGlowBuffer = Color(0xFF0A0A1A)
    
    // Gradient Pairs for each feature
    val WorkoutGradient = listOf(WorkoutCyan, WorkoutCyanLight)
    val CardioGradient = listOf(CardioRose, CardioRoseLight)
    val VitaminGradient = listOf(VitaminPurple, VitaminPurpleLight)
    val AnalyticsGradient = listOf(AnalyticsIndigo, AnalyticsIndigoLight)
}
