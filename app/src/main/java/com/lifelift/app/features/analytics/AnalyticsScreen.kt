package com.lifelift.app.features.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelift.app.R
import com.lifelift.app.core.ui.components.GlassyCard
import com.lifelift.app.core.ui.utils.rubberBand
import com.lifelift.app.core.ui.theme.AppColors
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptics = LocalHapticFeedback.current
    val accentColor = AppColors.AnalyticsIndigo

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AppColors.AnalyticsIndigo.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                stringResource(R.string.analytics_title),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            )
                            Text(
                                stringResource(R.string.analytics_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateBack()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.desc_back))
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.refresh()
                        }) {
                            Icon(Icons.Default.Refresh, "Refresh")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = accentColor)
            }
        } else {
            var selectedTab by remember { mutableIntStateOf(0) }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Fixed TabRow at top
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text(stringResource(R.string.tab_summary)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text(stringResource(R.string.tab_progress)) }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .clipToBounds()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .rubberBand()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            if (selectedTab == 0) {
                                DailyOverviewSection(uiState)
                            } else {
                                ProgressSection(uiState, viewModel)
                            }
                        }
                        
                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyOverviewSection(uiState: AnalyticsUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        
        // Header
        Text(
            text = formatDate(java.time.LocalDate.now().toString()), // Show Today's Date
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // 1. Daily Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Workouts Count
            StatCard(
                title = stringResource(R.string.card_workouts),
                value = "${uiState.dailyWorkoutsCount}",
                icon = Icons.Filled.CheckCircle,
                gradientColors = listOf(Color(0xFF00D9FF), Color(0xFF0099CC)),
                modifier = Modifier.weight(1f)
            )
            
            // Calories
            StatCard(
                title = stringResource(R.string.label_calories),
                value = "${uiState.dailyCalories}",
                icon = Icons.Filled.Star, // Fire icon replacement
                gradientColors = listOf(Color(0xFFFF8A65), Color(0xFFE57373)),
                modifier = Modifier.weight(1f)
            )
        }
        
        // 2. Volume
        GlassyCard(modifier = Modifier.fillMaxWidth(), accentColor = Color(0xFFAA00FF)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        stringResource(R.string.card_volume),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${uiState.dailyVolume} kg",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFAA00FF)
                    )
                }
                Icon(Icons.Filled.Favorite, null, tint = Color(0xFFAA00FF))
            }
        }

        // 3. Today's Workouts List
        if (uiState.todaysWorkouts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.section_recent_workouts), // Reuse string "Recent Workouts" or create "Today's Workouts"
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            uiState.todaysWorkouts.forEach { workout ->
                GlassyCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                workout.workout.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (workout.workout.notes.isNotEmpty()) {
                                Text(
                                    workout.workout.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        } else {
             // Empty State for Today
             Box(
                 modifier = Modifier.fillMaxWidth().padding(32.dp),
                 contentAlignment = Alignment.Center
             ) {
                 Text(
                     "No workouts today",
                     style = MaterialTheme.typography.bodyMedium,
                     color = MaterialTheme.colorScheme.onSurfaceVariant
                 )
             }
        }
    }
}

@Composable
fun ProgressSection(uiState: AnalyticsUiState, viewModel: AnalyticsViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        
        // Strength Progression (Interactive)
        ProgressionSection(
            availableExercises = uiState.availableExercises,
            selectedExercise = uiState.selectedExercise,
            progress = uiState.exerciseProgress,
            history = uiState.exerciseHistory,
            onExerciseSelected = { viewModel.selectExercise(it) }
        )

        // Weekly Volume Chart
        if (uiState.weeklyVolume.isNotEmpty()) {
            VolumeChartCard(
                title = stringResource(R.string.title_weekly_volume),
                subtitle = stringResource(R.string.subtitle_weekly_volume),
                data = uiState.weeklyVolume,
                barColor = Color(0xFF42A5F5)
            )
        } else {
             Text(stringResource(R.string.msg_no_data_weekly), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        // Monthly Volume Chart
        if (uiState.monthlyVolume.isNotEmpty()) {
            VolumeChartCard(
                title = stringResource(R.string.title_monthly_volume),
                subtitle = stringResource(R.string.subtitle_monthly_volume),
                data = uiState.monthlyVolume,
                barColor = Color(0xFFAB47BC)
            )
        } else {
            Text(stringResource(R.string.msg_no_data_monthly), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun VolumeChartCard(
    title: String,
    subtitle: String,
    data: List<VolumePoint>,
    barColor: Color
) {
    GlassyCard(
        modifier = Modifier.fillMaxWidth(),
        accentColor = barColor
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val barColor = MaterialTheme.colorScheme.primary
                val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
                
                // Pre-calculate points and paths outside of the DrawScope
                val points = remember(data) {
                    val maxVolRaw = data.maxOfOrNull { it.volume }?.toFloat() ?: 0f
                    val maxVol = if (maxVolRaw > 0f) maxVolRaw else 1f
                    
                    data.mapIndexed { index, point ->
                        val x = if (data.size > 1) {
                            index.toFloat() / (data.size - 1)
                        } else 0f
                        val y = 1f - (point.volume.toFloat() / maxVol)
                        androidx.compose.ui.geometry.Offset(x, y)
                    }
                }

                val chartPath = remember(points) {
                    androidx.compose.ui.graphics.Path().apply {
                        if (points.size > 1) {
                            // We will scale these 0..1 points to actual Canvas size inside draw phase
                        }
                    }
                }

                androidx.compose.foundation.Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithCache {
                            val scaledPoints = points.map { 
                                androidx.compose.ui.geometry.Offset(it.x * size.width, it.y * size.height) 
                            }

                            val scaledPath = androidx.compose.ui.graphics.Path().apply {
                                if (scaledPoints.size > 1) {
                                    moveTo(scaledPoints.first().x, scaledPoints.first().y)
                                    for (i in 0 until scaledPoints.size - 1) {
                                        val p1 = scaledPoints[i]
                                        val p2 = scaledPoints[i + 1]
                                        cubicTo(
                                            p1.x + (p2.x - p1.x) / 2, p1.y,
                                            p1.x + (p2.x - p1.x) / 2, p2.y,
                                            p2.x, p2.y
                                        )
                                    }
                                }
                            }

                            val fillPath = androidx.compose.ui.graphics.Path().apply {
                                if (scaledPoints.size > 1) {
                                    addPath(scaledPath)
                                    lineTo(size.width, size.height)
                                    lineTo(0f, size.height)
                                    close()
                                }
                            }

                            onDrawWithContent {
                                if (data.isEmpty()) return@onDrawWithContent

                                // Draw Grid
                                drawLine(
                                    color = onSurfaceVariant.copy(alpha = 0.2f),
                                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                                    strokeWidth = 1.dp.toPx()
                                )
                                drawLine(
                                    color = onSurfaceVariant.copy(alpha = 0.2f),
                                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                                    strokeWidth = 1.dp.toPx()
                                )

                                if (scaledPoints.size > 1) {
                                    drawPath(
                                        path = scaledPath,
                                        color = barColor,
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                                            width = 3.dp.toPx(),
                                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                                        )
                                    )
                                    
                                    drawPath(
                                        path = fillPath,
                                        brush = Brush.verticalGradient(
                                            colors = listOf(barColor.copy(alpha = 0.3f), Color.Transparent),
                                            startY = 0f,
                                            endY = size.height
                                        )
                                    )
                                } else if (scaledPoints.size == 1) {
                                    drawCircle(color = barColor, radius = 4.dp.toPx(), center = scaledPoints.first())
                                }
                                
                                scaledPoints.forEach { 
                                    drawCircle(color = barColor, radius = 4.dp.toPx(), center = it)
                                }
                            }
                        }
                ) { }
                
                // Labels (Improved X-Axis)
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    data.forEachIndexed { index, point ->
                        // Show first, last, and every ~3rd label to avoid clutter
                        val shouldShow = index == 0 || index == data.size - 1 || (data.size > 6 && index % (data.size / 4) == 0)
                        
                        if (shouldShow) {
                            // Parse date
                            val dateLabel = try {
                                if (point.date.length >= 7) point.date.substring(5) else point.date
                            } catch(e: Exception) { point.date }
                            
                            Text(
                                text = dateLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Spacer(modifier = Modifier.width(1.dp)) 
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    GlassyCard(
        modifier = modifier,
        accentColor = gradientColors.first()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressionSection(
    availableExercises: List<String>,
    selectedExercise: String?,
    progress: ExerciseProgress?,
    history: List<ExerciseHistoryPoint>,
    onExerciseSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    GlassyCard(
        modifier = Modifier.fillMaxWidth(),
        accentColor = MaterialTheme.colorScheme.primary
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Header + Dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.label_strength_progress),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .clickable { expanded = true }
                                .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                        ) {
                            Text(
                                selectedExercise ?: stringResource(R.string.label_select_exercise),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            availableExercises.forEach { exercise ->
                                DropdownMenuItem(
                                    text = { Text(exercise) },
                                    onClick = {
                                        onExerciseSelected(exercise)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Big Percentage Label
                if (progress != null) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "${if (progress.diffKm >= 0) "+" else ""}${progress.diffKm}kg",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (progress.diffKm >= 0) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error // Green/Red
                        )
                        Text(
                            "(${if (progress.diffPercent >= 0) "+" else ""}${String.format("%.1f", progress.diffPercent)}%)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // New Canvas-based Line Chart
            if (history.isNotEmpty()) {
                val primaryColor = MaterialTheme.colorScheme.primary
                val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp)
                ) {
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
                    
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawWithCache {
                                val maxVal = history.maxOfOrNull { it.maxWeight }?.toFloat() ?: 100f
                                val minVal = (history.minOfOrNull { it.maxWeight }?.times(0.8f))?.toFloat() ?: 0f
                                val range = (maxVal - minVal).coerceAtLeast(1f)

                                val points = history.mapIndexed { index, point ->
                                    val x = size.width * (index.toFloat() / (history.size - 1).coerceAtLeast(1))
                                    val y = size.height - (size.height * ((point.maxWeight.toFloat() - minVal) / range))
                                    androidx.compose.ui.geometry.Offset(x, y)
                                }

                                val path = androidx.compose.ui.graphics.Path().apply {
                                    if (points.size > 1) {
                                        moveTo(points.first().x, points.first().y)
                                        for (i in 0 until points.size - 1) {
                                            val p1 = points[i]
                                            val p2 = points[i + 1]
                                            cubicTo(
                                                p1.x + (p2.x - p1.x) / 2, p1.y,
                                                p1.x + (p2.x - p1.x) / 2, p2.y,
                                                p2.x, p2.y
                                            )
                                        }
                                    }
                                }

                                val fillPath = androidx.compose.ui.graphics.Path().apply {
                                    if (points.size > 1) {
                                        addPath(path)
                                        lineTo(size.width, size.height)
                                        lineTo(0f, size.height)
                                        close()
                                    }
                                }

                                onDrawWithContent {
                                    if (history.isEmpty()) return@onDrawWithContent

                                    // Draw grid lines
                                    drawLine(
                                        color = onSurfaceVariant.copy(alpha = 0.2f),
                                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                        end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                    drawLine(
                                        color = onSurfaceVariant.copy(alpha = 0.2f),
                                        start = androidx.compose.ui.geometry.Offset(0f, size.height),
                                        end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                                        strokeWidth = 1.dp.toPx()
                                    )

                                    if (points.size > 1) {
                                        drawPath(
                                            path = path,
                                            color = primaryColor,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                                width = 3.dp.toPx(),
                                                cap = androidx.compose.ui.graphics.StrokeCap.Round
                                            )
                                        )

                                        drawPath(
                                            path = fillPath,
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    primaryColor.copy(alpha = 0.3f),
                                                    Color.Transparent
                                                ),
                                                startY = 0f,
                                                endY = size.height
                                            )
                                        )
                                    } else if (points.size == 1) {
                                        drawCircle(
                                            color = primaryColor,
                                            radius = 4.dp.toPx(),
                                            center = points.first()
                                        )
                                    }

                                    points.forEachIndexed { index, point ->
                                        drawCircle(
                                            color = primaryColor,
                                            radius = 4.dp.toPx(),
                                            center = point
                                        )
                                    }
                                }
                            }
                    ) { }

                    // Overlay Date Labels using Row to ensure layout support
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        history.forEachIndexed { index, point ->
                            // Show only some labels if too many
                            if (history.size <= 5 || index % (history.size / 4) == 0 || index == history.size - 1) {
                                Text(
                                    text = formatDateShort(point.date),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }
                        }
                    }
                }
            } else {
                Text(
                    stringResource(R.string.msg_no_data_available),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        }
    }
}

private fun formatDateShort(isoDateTime: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoDateTime)
        dateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
    } catch (e: Exception) {
        ""
    }
}

private fun formatDate(isoDateTime: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoDateTime)
        dateTime.format(DateTimeFormatter.ofPattern("MMM d"))
    } catch (e: Exception) {
        isoDateTime.take(10)
    }
}

