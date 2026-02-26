package com.lifelift.app.core.data.local.dao

import androidx.room.*
import com.lifelift.app.core.data.local.entity.ProgramEntity
import com.lifelift.app.core.data.local.entity.ProgramExerciseEntity
import com.lifelift.app.core.data.local.entity.ProgramWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Transaction
    @Query("SELECT * FROM programs")
    fun getAllPrograms(): Flow<List<ProgramWithExercises>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: ProgramEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramExercise(exercise: ProgramExerciseEntity): Long
    
    @Delete
    suspend fun deleteProgram(program: ProgramEntity)
}
