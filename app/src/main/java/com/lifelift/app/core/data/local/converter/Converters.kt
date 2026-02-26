package com.lifelift.app.core.data.local.converter

import androidx.room.TypeConverter
import com.lifelift.app.core.data.local.entity.VitaminFrequency
import com.lifelift.app.core.data.local.entity.WorkoutType

class Converters {
    @TypeConverter
    fun fromVitaminFrequency(value: VitaminFrequency): String {
        return value.name
    }

    @TypeConverter
    fun toVitaminFrequency(value: String): VitaminFrequency {
        return try {
            VitaminFrequency.valueOf(value)
        } catch (e: IllegalArgumentException) {
            VitaminFrequency.DAILY
        }
    }
    @TypeConverter
    fun fromWorkoutType(value: WorkoutType): String {
        return value.name
    }

    @TypeConverter
    fun toWorkoutType(value: String): WorkoutType {
        return try {
            WorkoutType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            WorkoutType.STRENGTH
        }
    }
}
