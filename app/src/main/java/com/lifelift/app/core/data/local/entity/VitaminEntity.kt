package com.lifelift.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifelift.app.core.util.DateUtils
/**
 * Represents a daily vitamin or supplement with scheduling
 */
@Entity(tableName = "vitamins")
data class VitaminEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // e.g., "Vitamin D3", "Omega-3"
    val dosage: String, // e.g., "1000 IU", "2 capsules"
    val timeOfDay: String, // e.g., "08:00", "20:00" (24-hour format)
    val frequency: VitaminFrequency = VitaminFrequency.DAILY,
    val reminderEnabled: Boolean = true,
    val color: String = "#6EFFC4", // Hex color for UI personalization
    val notes: String = "",
    val createdAt: String = DateUtils.getCurrentDateTime()
)

/**
 * Daily Vitamin Check Log
 * Tracks whether a vitamin was taken on a specific date
 */
@Entity(tableName = "vitamin_logs")
data class VitaminLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vitaminId: Long,
    val date: String, // "2026-01-19"
    val taken: Boolean = false,
    val takenAt: String? = null // Timestamp when marked as taken
)

enum class VitaminFrequency {
    DAILY,
    WEEKLY,
    AS_NEEDED
}
