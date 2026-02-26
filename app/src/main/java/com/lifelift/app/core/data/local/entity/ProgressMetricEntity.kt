package com.lifelift.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Progress Metric Entity
 * Stores various body metrics and performance data for analytics
 */
@Entity(tableName = "progress_metrics")
data class ProgressMetricEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // "2026-01-19"
    val metricType: MetricType,
    val value: Double,
    val unit: String, // e.g., "kg", "lbs", "cm", "%"
    val notes: String = ""
)

enum class MetricType {
    BODY_WEIGHT,
    BODY_FAT_PERCENTAGE,
    MUSCLE_MASS,
    
    // Exercise PRs (Personal Records)
    BENCH_PRESS_MAX,
    SQUAT_MAX,
    DEADLIFT_MAX,
    
    // Volume metrics
    WEEKLY_VOLUME,
    MONTHLY_VOLUME,
    
    // Custom
    CUSTOM
}

/**
 * Extension for analytics calculations
 */
fun List<ProgressMetricEntity>.getProgressPercentage(metricType: MetricType): Double? {
    val metrics = this.filter { it.metricType == metricType }.sortedBy { it.date }
    if (metrics.size < 2) return null
    
    val first = metrics.first().value
    val last = metrics.last().value
    
    return if (first != 0.0) {
        ((last - first) / first) * 100
    } else {
        null
    }
}
