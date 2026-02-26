package com.lifelift.app.core.di

import android.content.Context
import androidx.room.Room
import com.lifelift.app.core.data.local.LifeLiftDatabase
import com.lifelift.app.core.data.local.dao.ProgressMetricDao
import com.lifelift.app.core.data.local.dao.VitaminDao
import com.lifelift.app.core.data.local.dao.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LifeLiftDatabase {
        return Room.databaseBuilder(
            context,
            LifeLiftDatabase::class.java,
            "lifelift_database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : androidx.room.RoomDatabase.Callback() {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Bench Press', 'Chest', 90)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Squat', 'Legs', 120)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Deadlift', 'Back', 120)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Overhead Press', 'Shoulders', 90)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Pull Up', 'Back', 60)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Dumbbell Curl', 'Biceps', 45)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Tricep Extension', 'Triceps', 45)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Lunges', 'Legs', 60)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Plank', 'Core', 0)")
                    db.execSQL("INSERT INTO exercise_refs (name, category, defaultRestSeconds) VALUES ('Push Up', 'Chest', 45)")
                }
            })
            .build()
    }
    
    @Provides
    fun provideWorkoutDao(database: LifeLiftDatabase): WorkoutDao {
        return database.workoutDao()
    }
    
    @Provides
    fun provideVitaminDao(database: LifeLiftDatabase): VitaminDao {
        return database.vitaminDao()
    }
    
    @Provides
    fun provideProgressMetricDao(database: LifeLiftDatabase): ProgressMetricDao {
        return database.progressMetricDao()
    }

    @Provides
    fun provideProgramDao(database: LifeLiftDatabase): com.lifelift.app.core.data.local.dao.ProgramDao {
        return database.programDao()
    }
}
