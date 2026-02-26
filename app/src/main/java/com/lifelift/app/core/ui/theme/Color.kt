package com.lifelift.app.core.ui.theme

import androidx.compose.ui.graphics.Color

// Dark Theme Colors (Primary)
object DarkColors {
    val Background = Color(0xFF121212)
    val Surface = Color(0xFF1E1E1E)
    val SurfaceElevated = Color(0xFF2A2A2A)
    val SurfaceCard = Color(0xFF2A2A2A) // Solid card background
    val SurfaceGlass = Color(0x80252525) // Semi-transparent for glassmorphism
    
    // Accent Colors
    val NeonBlue = Color(0xFF00D9FF) // Gym/Iron module
    val NeonBlueDim = Color(0xFF0099CC)
    val SoftMint = Color(0xFF6EFFC4) // Vitamin/Vitality module
    val SoftMintDim = Color(0xFF4DCC9A)
    val Purple = Color(0xFFBB86FC) // Analytics/Progress
    val PurpleDim = Color(0xFF9965D4)
    
    // Text Colors
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB3B3B3)
    val TextTertiary = Color(0xFF808080)
    
    // Utility Colors
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFFB347)
    val Error = Color(0xFFFF5252)
    val Divider = Color(0xFF2E2E2E)
}

// AMOLED Theme Colors (Pure Black)
object AmoledColors {
    val Background = Color(0xFF000000)
    val Surface = Color(0xFF0A0A0A)
    val SurfaceElevated = Color(0xFF151515)
    val SurfaceCard = Color(0xFF151515) // Solid card background
    val SurfaceGlass = Color(0x80101010)
    
    // Accent Colors (same as dark)
    val NeonBlue = Color(0xFF00D9FF)
    val NeonBlueDim = Color(0xFF0099CC)
    val SoftMint = Color(0xFF6EFFC4)
    val SoftMintDim = Color(0xFF4DCC9A)
    val Purple = Color(0xFFBB86FC)
    val PurpleDim = Color(0xFF9965D4)
    
    // Text Colors
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB3B3B3)
    val TextTertiary = Color(0xFF808080)
    
    // Utility Colors
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFFB347)
    val Error = Color(0xFFFF5252)
    val Divider = Color(0xFF1A1A1A)
}

// Light Theme Colors (Secondary)
object LightColors {
    val Background = Color(0xFFF5F5F7)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceElevated = Color(0xFFFAFAFA)
    val SurfaceCard = Color(0xFFFFFFFF) // Solid card background
    val SurfaceGlass = Color(0xCCFFFFFF)
    
    // Accent Colors (darker for light mode)
    val NeonBlue = Color(0xFF007AFF)
    val NeonBlueDim = Color(0xFF0055CC)
    val SoftMint = Color(0xFF30D158)
    val SoftMintDim = Color(0xFF28A745)
    val Purple = Color(0xFF6200EE)
    val PurpleDim = Color(0xFF4B00B5)
    
    // Text Colors
    val TextPrimary = Color(0xFF000000)
    val TextSecondary = Color(0xFF666666)
    val TextTertiary = Color(0xFF999999)
    
    // Utility Colors
    val Success = Color(0xFF34C759)
    val Warning = Color(0xFFFF9500)
    val Error = Color(0xFFFF3B30)
    val Divider = Color(0xFFE5E5EA)
}

// Semantic Color Mappings
object SemanticColors {
    // Module-specific colors
    val GymPrimary = DarkColors.NeonBlue
    val GymSecondary = DarkColors.NeonBlueDim
    val VitaminPrimary = DarkColors.SoftMint
    val VitaminSecondary = DarkColors.SoftMintDim
    val AnalyticsPrimary = DarkColors.Purple
    val AnalyticsSecondary = DarkColors.PurpleDim
}
