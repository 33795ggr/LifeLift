package com.lifelift.app.core.data.local.dao

import androidx.room.*
import com.lifelift.app.core.data.local.entity.MetricType
import com.lifelift.app.core.data.local.entity.ProgressMetricEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Progress Metrics operations
 */
@Dao
interface ProgressMetricDao {
    
    @Query("SELECT * FROM progress_metrics ORDER BY date DESC")
    fun getAllMetrics(): Flow<List<ProgressMetricEntity>>
    
    @Query("SELECT * FROM progress_metrics WHERE metricType = :type ORDER BY date DESC")
    fun getMetricsByType(type: MetricType): Flow<List<ProgressMetricEntity>>
    
    @Query("SELECT * FROM progress_metrics WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getMetricsForPeriod(startDate: String, endDate: String): Flow<List<ProgressMetricEntity>>
    
    @Query("SELECT * FROM progress_metrics WHERE metricType = :type AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getMetricsByTypeForPeriod(
        type: MetricType,
        startDate: String,
        endDate: String
    ): Flow<List<ProgressMetricEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: ProgressMetricEntity): Long
    
    @Update
    suspend fun updateMetric(metric: ProgressMetricEntity)
    
    @Delete
    suspend fun deleteMetric(metric: ProgressMetricEntity)
    
    @Query("DELETE FROM progress_metrics WHERE id = :id")
    suspend fun deleteMetricById(id: Long)
}
