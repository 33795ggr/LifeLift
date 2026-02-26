package com.lifelift.app.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lifelift.app.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lifelift.app.core.ui.components.FeatureGlassCard
import com.lifelift.app.core.ui.theme.AppColors
import com.lifelift.app.core.ui.utils.rubberBand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToWorkouts: () -> Unit,
    onNavigateToCardio: () -> Unit,
    onNavigateToVitamins: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val haptics = LocalHapticFeedback.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().clipToBounds()) {
            // Background gradient glow (Solid to Solid for performance)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AppColors.WorkoutGlow,
                                Color.Black
                            )
                        )
                    )
            )
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .rubberBand(),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 20.dp,
                    bottom = paddingValues.calculateBottomPadding() + 20.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                stringResource(R.string.app_name),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp
                            )
                            Text(
                                stringResource(R.string.home_subtitle),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Settings button with accent border
                        IconButton(
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateToSettings()
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(AppColors.CardDark)
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = stringResource(R.string.desc_settings),
                                tint = AppColors.TextLightGray
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Workouts Card
                item {
                    PremiumFeatureCard(
                        title = stringResource(R.string.feature_workouts_title),
                        subtitle = stringResource(R.string.feature_workouts_subtitle),
                        icon = Icons.Rounded.Favorite,
                        accentColor = AppColors.WorkoutCyan,
                        gradientColors = AppColors.WorkoutGradient,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToWorkouts()
                        }
                    )
                }

                // Cardio Card
                item {
                    PremiumFeatureCard(
                        title = stringResource(R.string.feature_cardio_title),
                        subtitle = stringResource(R.string.feature_cardio_subtitle),
                        icon = Icons.Rounded.PlayArrow,
                        accentColor = AppColors.CardioRose,
                        gradientColors = AppColors.CardioGradient,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToCardio()
                        }
                    )
                }

                // Vitamins Card
                item {
                    PremiumFeatureCard(
                        title = stringResource(R.string.feature_vitamins_title),
                        subtitle = stringResource(R.string.feature_vitamins_subtitle),
                        icon = Icons.Rounded.Star,
                        accentColor = AppColors.VitaminPurple,
                        gradientColors = AppColors.VitaminGradient,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToVitamins()
                        }
                    )
                }

                // Analytics Card
                item {
                    PremiumFeatureCard(
                        title = stringResource(R.string.feature_analytics_title),
                        subtitle = stringResource(R.string.feature_analytics_subtitle),
                        icon = Icons.Rounded.DateRange,
                        accentColor = AppColors.AnalyticsIndigo,
                        gradientColors = AppColors.AnalyticsGradient,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToAnalytics()
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun PremiumFeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    FeatureGlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        accentColor = accentColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Icon with gradient background
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(gradientColors)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.desc_navigate),
                tint = accentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
