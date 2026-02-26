package com.lifelift.app.core.data.local.dao

import androidx.room.*
import com.lifelift.app.core.data.local.entity.VitaminEntity
import com.lifelift.app.core.data.local.entity.VitaminLogEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Vitamin operations
 */
@Dao
interface VitaminDao {
    
    // Vitamin CRUD
    @Query("SELECT * FROM vitamins ORDER BY timeOfDay ASC")
    fun getAllVitamins(): Flow<List<VitaminEntity>>
    
    @Query("SELECT * FROM vitamins WHERE id = :id")
    suspend fun getVitaminById(id: Long): VitaminEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVitamin(vitamin: VitaminEntity): Long
    
    @Update
    suspend fun updateVitamin(vitamin: VitaminEntity)
    
    @Delete
    suspend fun deleteVitamin(vitamin: VitaminEntity)
    
    // Vitamin Log CRUD
    @Query("SELECT * FROM vitamin_logs WHERE date = :date")
    fun getLogsForDate(date: String): Flow<List<VitaminLogEntity>>
    
    @Query("SELECT * FROM vitamin_logs WHERE vitaminId = :vitaminId AND date = :date")
    suspend fun getLogForVitaminAndDate(vitaminId: Long, date: String): VitaminLogEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: VitaminLogEntity): Long
    
    @Update
    suspend fun updateLog(log: VitaminLogEntity)
    
    @Query("DELETE FROM vitamin_logs WHERE vitaminId = :vitaminId")
    suspend fun deleteLogsForVitamin(vitaminId: Long)
    
    // Analytics
    @Query("SELECT COUNT(*) FROM vitamin_logs WHERE date BETWEEN :startDate AND :endDate AND taken = 1")
    suspend fun getTakenCountForPeriod(startDate: String, endDate: String): Int
    
    @Query("SELECT COUNT(*) FROM vitamin_logs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalCountForPeriod(startDate: String, endDate: String): Int
}
