package com.lifelift.app.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lifelift.app.core.data.local.converter.Converters
import com.lifelift.app.core.data.local.dao.ProgressMetricDao
import com.lifelift.app.core.data.local.dao.VitaminDao
import com.lifelift.app.core.data.local.dao.WorkoutDao
import com.lifelift.app.core.data.local.entity.ExerciseEntity
import com.lifelift.app.core.data.local.entity.ExerciseRefEntity
import com.lifelift.app.core.data.local.entity.ProgramEntity
import com.lifelift.app.core.data.local.entity.ProgramExerciseEntity
import com.lifelift.app.core.data.local.entity.ProgressMetricEntity
import com.lifelift.app.core.data.local.entity.SetEntity
import com.lifelift.app.core.data.local.entity.VitaminEntity
import com.lifelift.app.core.data.local.entity.VitaminLogEntity
import com.lifelift.app.core.data.local.entity.WorkoutEntity

/**
 * LifeLift Room Database
 */
@Database(
    entities = [
        WorkoutEntity::class,
        ExerciseEntity::class,
        SetEntity::class,
        ExerciseRefEntity::class,
        ProgramEntity::class,
        ProgramExerciseEntity::class,
        VitaminEntity::class,
        VitaminLogEntity::class,
        ProgressMetricEntity::class
    ],
    version = 4, // Incremented version
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LifeLiftDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun vitaminDao(): VitaminDao
    abstract fun progressMetricDao(): ProgressMetricDao
    abstract fun programDao(): com.lifelift.app.core.data.local.dao.ProgramDao
}
