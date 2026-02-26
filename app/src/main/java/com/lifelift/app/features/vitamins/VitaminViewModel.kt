package com.lifelift.app.features.vitamins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelift.app.core.data.local.entity.VitaminEntity
import com.lifelift.app.core.data.local.entity.VitaminFrequency
import com.lifelift.app.core.data.local.entity.VitaminLogEntity
import com.lifelift.app.core.data.repository.VitaminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.lifelift.app.core.notification.VitaminNotificationScheduler

data class VitaminWithStatus(
    val vitamin: VitaminEntity,
    val isTaken: Boolean
)

data class VitaminUiState(
    val vitamins: List<VitaminWithStatus> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val todayDate: String = ""
)

@HiltViewModel
class VitaminViewModel @Inject constructor(
    private val vitaminRepository: VitaminRepository,
    private val notificationScheduler: VitaminNotificationScheduler
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(VitaminUiState())
    val uiState: StateFlow<VitaminUiState> = _uiState.asStateFlow()
    
    init {
        loadVitamins()
    }
    
    private fun loadVitamins() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val todayDate = vitaminRepository.getTodayDateString()
            _uiState.update { it.copy(todayDate = todayDate) }
            
            try {
                combine(
                    vitaminRepository.getAllVitamins(),
                    vitaminRepository.getLogsForDate(todayDate)
                ) { vitamins, logs ->
                    vitamins.map { vitamin ->
                        VitaminWithStatus(
                            vitamin = vitamin,
                            isTaken = logs.find { it.vitaminId == vitamin.id }?.taken == true
                        )
                    }
                }.catch { e ->
                    _uiState.update { 
                        it.copy(isLoading = false, error = e.message)
                    }
                }.collect { vitaminsWithStatus ->
                    _uiState.update {
                        it.copy(
                            vitamins = vitaminsWithStatus,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
    
    fun addVitamin(
        name: String,
        dosage: String,
        timeOfDay: String,
        frequency: VitaminFrequency = VitaminFrequency.DAILY,
        notes: String = "",
        enableNotification: Boolean = true
    ) {
        viewModelScope.launch {
            val vitamin = VitaminEntity(
                name = name,
                dosage = dosage,
                timeOfDay = timeOfDay,
                frequency = frequency,
                notes = notes
            )
            
            try {
                val id = vitaminRepository.insertVitamin(vitamin)
                if (enableNotification) {
                    notificationScheduler.scheduleNotification(vitamin.copy(id = id))
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    fun toggleVitaminTaken(vitaminId: Long) {
        viewModelScope.launch {
            try {
                vitaminRepository.toggleVitaminTaken(vitaminId, _uiState.value.todayDate)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    fun deleteVitamin(vitamin: VitaminEntity) {
        viewModelScope.launch {
            try {
                notificationScheduler.cancelNotification(vitamin)
                vitaminRepository.deleteVitamin(vitamin)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
