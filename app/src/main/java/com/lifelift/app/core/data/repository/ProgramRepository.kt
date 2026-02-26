package com.lifelift.app.core.data.repository

import com.lifelift.app.core.data.local.dao.ProgramDao
import com.lifelift.app.core.data.local.entity.ProgramEntity
import com.lifelift.app.core.data.local.entity.ProgramExerciseEntity
import com.lifelift.app.core.data.local.entity.ProgramWithExercises
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgramRepository @Inject constructor(
    private val programDao: ProgramDao
) {
    fun getAllPrograms(): Flow<List<ProgramWithExercises>> {
        return programDao.getAllPrograms()
    }

    suspend fun insertProgram(program: ProgramEntity): Long {
        return programDao.insertProgram(program)
    }

    suspend fun insertProgramExercise(exercise: ProgramExerciseEntity): Long {
        return programDao.insertProgramExercise(exercise)
    }
    
    suspend fun deleteProgram(program: ProgramEntity) {
        programDao.deleteProgram(program)
    }
}
