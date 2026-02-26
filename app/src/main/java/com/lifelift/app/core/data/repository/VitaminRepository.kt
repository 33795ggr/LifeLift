package com.lifelift.app.core.data.repository

import com.lifelift.app.core.data.local.dao.VitaminDao
import com.lifelift.app.core.data.local.entity.VitaminEntity
import com.lifelift.app.core.data.local.entity.VitaminLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VitaminRepository @Inject constructor(
    private val vitaminDao: VitaminDao
) {
    fun getAllVitamins(): Flow<List<VitaminEntity>> {
        return vitaminDao.getAllVitamins()
    }
    
    suspend fun getVitaminById(id: Long): VitaminEntity? {
        return vitaminDao.getVitaminById(id)
    }
    
    suspend fun insertVitamin(vitamin: VitaminEntity): Long {
        return vitaminDao.insertVitamin(vitamin)
    }
    
    suspend fun updateVitamin(vitamin: VitaminEntity) {
        vitaminDao.updateVitamin(vitamin)
    }
    
    suspend fun deleteVitamin(vitamin: VitaminEntity) {
        vitaminDao.deleteLogsForVitamin(vitamin.id)
        vitaminDao.deleteVitamin(vitamin)
    }
    
    fun getLogsForDate(date: String): Flow<List<VitaminLogEntity>> {
        return vitaminDao.getLogsForDate(date)
    }
    
    suspend fun toggleVitaminTaken(vitaminId: Long, date: String) {
        val existingLog = vitaminDao.getLogForVitaminAndDate(vitaminId, date)
        
        if (existingLog != null) {
            vitaminDao.updateLog(
                existingLog.copy(
                    taken = !existingLog.taken,
                    takenAt = if (!existingLog.taken) LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) else null
                )
            )
        } else {
            vitaminDao.insertLog(
                VitaminLogEntity(
                    vitaminId = vitaminId,
                    date = date,
                    taken = true,
                    takenAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )
            )
        }
    }
    
    suspend fun isVitaminTakenOnDate(vitaminId: Long, date: String): Boolean {
        return vitaminDao.getLogForVitaminAndDate(vitaminId, date)?.taken == true
    }
    
    suspend fun getTakenCountForPeriod(startDate: String, endDate: String): Int {
        return vitaminDao.getTakenCountForPeriod(startDate, endDate)
    }
    
    suspend fun getTotalCountForPeriod(startDate: String, endDate: String): Int {
        return vitaminDao.getTotalCountForPeriod(startDate, endDate)
    }
    
    fun getTodayDateString(): String {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
