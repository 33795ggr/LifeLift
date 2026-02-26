package com.lifelift.app.features.vitamins

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelift.app.R
import com.lifelift.app.core.data.local.entity.VitaminEntity
import com.lifelift.app.core.data.local.entity.VitaminFrequency
import com.lifelift.app.core.ui.components.GlassyCard
import com.lifelift.app.core.ui.utils.rubberBand
import com.lifelift.app.core.ui.theme.AppColors

// Alias to AppColors for readability
private val VitaminPurple get() = AppColors.VitaminPurple
private val HealthGreen get() = AppColors.SuccessGreen
private val PillOrange get() = AppColors.WarningOrange
private val EnergyYellow get() = Color(0xFFFFCC00)
private val MineralBlue get() = Color(0xFF5AC8FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitaminsScreen(
    onNavigateBack: () -> Unit,
    viewModel: VitaminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptics = LocalHapticFeedback.current
    val context = androidx.compose.ui.platform.LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }

    // Permission Logic for Notifications (Android 13+)
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission(),
        onResult = { /* Permission handled */ }
    )

    LaunchedEffect(Unit) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            // Premium Header with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                VitaminPurple.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Animated Pill Icon
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(PillOrange, EnergyYellow)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    stringResource(R.string.vitamins_title),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    stringResource(R.string.vitamins_subtitle),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        floatingActionButton = {
            // Premium Gradient FAB
            FloatingActionButton(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        val alarmManager = context.getSystemService(android.content.Context.ALARM_SERVICE) as android.app.AlarmManager
                        if (!alarmManager.canScheduleExactAlarms()) {
                            val intent = android.content.Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                            context.startActivity(intent)
                            return@FloatingActionButton
                        }
                    }
                    showAddDialog = true
                },
                containerColor = VitaminPurple,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .size(64.dp)
                    .shadow(12.dp, RoundedCornerShape(18.dp))
            ) {
                Icon(
                    Icons.Default.Add,
                    stringResource(R.string.btn_add_vitamin),
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = VitaminPurple,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 3.dp,
                        color = VitaminPurple
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { 
                        Text(
                            "Сегодня",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        ) 
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { 
                        Text(
                            "Все витамины",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        ) 
                    }
                )
            }
            
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = VitaminPurple
                    )
                } else if (uiState.vitamins.isEmpty()) {
                    EmptyVitaminsState(
                        modifier = Modifier.align(Alignment.Center),
                        onAddClick = { showAddDialog = true }
                    )
                } else {
                    // Simple switch without animation for better FPS
                    when (selectedTab) {
                        0 -> TodayTab(
                            vitamins = uiState.vitamins,
                            onToggle = { viewModel.toggleVitaminTaken(it) },
                            onDelete = { viewModel.deleteVitamin(it) },
                            haptics = haptics
                        )
                        1 -> AllVitaminsTab(
                            vitamins = uiState.vitamins,
                            onToggle = { viewModel.toggleVitaminTaken(it) },
                            onDelete = { viewModel.deleteVitamin(it) },
                            haptics = haptics
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddVitaminSheet(
            accentColor = VitaminPurple,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, dosage, time, notes, useAlarm, useNotification ->
                viewModel.addVitamin(
                    name = name,
                    dosage = dosage,
                    timeOfDay = time,
                    frequency = VitaminFrequency.DAILY,
                    notes = notes,
                    enableNotification = useNotification
                )

                if (useAlarm) {
                    try {
                        val parts = time.split(":")
                        val hour = parts[0].toIntOrNull() ?: 9
                        val minute = parts[1].toIntOrNull() ?: 0

                        val intent = android.content.Intent(android.provider.AlarmClock.ACTION_SET_ALARM).apply {
                            putExtra(android.provider.AlarmClock.EXTRA_MESSAGE, "Принять $name - $dosage")
                            putExtra(android.provider.AlarmClock.EXTRA_HOUR, hour)
                            putExtra(android.provider.AlarmClock.EXTRA_MINUTES, minute)
                            putExtra(android.provider.AlarmClock.EXTRA_SKIP_UI, true)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Ignore if no alarm app found
                    }
                }

                showAddDialog = false
            }
        )
    }
}

@Composable
private fun TodayTab(
    vitamins: List<VitaminWithStatus>,
    onToggle: (Long) -> Unit,
    onDelete: (VitaminEntity) -> Unit,
    haptics: androidx.compose.ui.hapticfeedback.HapticFeedback
) {
    val takenCount = vitamins.count { it.isTaken }
    val totalCount = vitamins.size
    val progress = if (totalCount > 0) takenCount.toFloat() / totalCount else 0f
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .rubberBand(),
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 100.dp,
            start = 20.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress Hero Card
        item {
            ProgressHeroCard(
                takenCount = takenCount,
                totalCount = totalCount,
                progress = progress
            )
        }
        
        // Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Расписание на сегодня",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$takenCount из $totalCount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Vitamin Items - Sorted by time
        val sortedVitamins = vitamins.sortedBy { it.vitamin.timeOfDay }
        items(
            items = sortedVitamins,
            key = { it.vitamin.id }
        ) { item ->
            PremiumVitaminCard(
                vitamin = item.vitamin,
                isTaken = item.isTaken,
                onToggle = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggle(item.vitamin.id)
                },
                onDelete = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDelete(item.vitamin)
                }
            )
        }
    }
}

@Composable
private fun AllVitaminsTab(
    vitamins: List<VitaminWithStatus>,
    onToggle: (Long) -> Unit,
    onDelete: (VitaminEntity) -> Unit,
    haptics: androidx.compose.ui.hapticfeedback.HapticFeedback
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .rubberBand(),
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 100.dp,
            start = 20.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Stats Summary
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniStatCard(
                    title = "Всего",
                    value = "${vitamins.size}",
                    icon = Icons.Default.Star,
                    color = VitaminPurple,
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    title = "Сегодня",
                    value = "${vitamins.count { it.isTaken }}/${vitamins.size}",
                    icon = Icons.Default.Check,
                    color = HealthGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Ваши витамины",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(
            items = vitamins.sortedBy { it.vitamin.name },
            key = { it.vitamin.id }
        ) { item ->
            CompactVitaminCard(
                vitamin = item.vitamin,
                isTaken = item.isTaken,
                onToggle = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggle(item.vitamin.id)
                },
                onDelete = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDelete(item.vitamin)
                }
            )
        }
    }
}

@Composable
private fun ProgressHeroCard(
    takenCount: Int,
    totalCount: Int,
    progress: Float
) {
    // Reduced animation for better performance
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progress"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            VitaminPurple.copy(alpha = 0.9f),
                            Color(0xFF7C3AED)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Прогресс сегодня",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "$takenCount из $totalCount",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(animatedProgress)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
                
                // Circular Progress
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 8.dp,
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Text(
                        "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumVitaminCard(
    vitamin: VitaminEntity,
    isTaken: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val cardColor = remember(vitamin.name) {
        when {
            vitamin.name.contains("D", ignoreCase = true) -> EnergyYellow
            vitamin.name.contains("C", ignoreCase = true) -> PillOrange
            vitamin.name.contains("B", ignoreCase = true) -> MineralBlue
            vitamin.name.contains("омега", ignoreCase = true) -> HealthGreen
            else -> VitaminPurple
        }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isTaken) 
                cardColor.copy(alpha = 0.15f) 
            else 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Check Circle
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        if (isTaken)
                            Brush.linearGradient(listOf(cardColor, cardColor.copy(alpha = 0.7f)))
                        else
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                    )
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isTaken) Icons.Default.Check else Icons.Default.Star,
                    contentDescription = null,
                    tint = if (isTaken) Color.White else cardColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vitamin.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (isTaken) TextDecoration.LineThrough else null,
                    color = if (isTaken) 
                        MaterialTheme.colorScheme.onSurfaceVariant 
                    else 
                        MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Dosage Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(cardColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            vitamin.dosage,
                            style = MaterialTheme.typography.labelSmall,
                            color = cardColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Time
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        vitamin.timeOfDay,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Delete Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun CompactVitaminCard(
    vitamin: VitaminEntity,
    isTaken: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    GlassyCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isTaken,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = HealthGreen,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    vitamin.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (isTaken) TextDecoration.LineThrough else null
                )
                Text(
                    "${vitamin.dosage} • ${vitamin.timeOfDay}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun MiniStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassyCard(
        modifier = modifier,
        accentColor = color
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyVitaminsState(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Animated Icon Container
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            VitaminPurple.copy(alpha = 0.2f),
                            EnergyYellow.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = VitaminPurple
            )
        }

        Text(
            text = stringResource(R.string.empty_vitamins_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.empty_vitamins_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VitaminPurple),
            modifier = Modifier
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp))
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                stringResource(R.string.btn_add_first_vitamin),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}
