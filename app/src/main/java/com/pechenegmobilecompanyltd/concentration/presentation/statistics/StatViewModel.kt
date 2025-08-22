package com.pechenegmobilecompanyltd.concentration.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pechenegmobilecompanyltd.concentration.domain.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class StatState(
    val todaySessions: Int = 0,
    val todayFocusTime: Int = 0,
    val weekSessions: Int = 0,
    val weekFocusTime: Int = 0
)

class StatViewModel : ViewModel(), KoinComponent {
    private val repository: SessionRepository by inject()

    private val _uiState = MutableStateFlow(StatState())
    val uiState: StateFlow<StatState> = _uiState

    fun loadStats() {
        viewModelScope.launch {
            val todayResult = repository.getTodaySessions()
            todayResult.onSuccess { sessions ->
                val workSessions = sessions.filter { it.type == "work" }
                val focusTime = workSessions.sumOf { it.duration }

                _uiState.value = _uiState.value.copy(
                    todaySessions = workSessions.size,
                    todayFocusTime = focusTime
                )
            }

            val allSessionsResult = repository.getSessions()
            allSessionsResult.onSuccess { sessions ->
                val calendar = java.util.Calendar.getInstance()
                calendar.add(java.util.Calendar.DAY_OF_YEAR, -7)
                val weekStart = calendar.time

                val weekSessions = sessions.filter { it.startTime.after(weekStart) }
                val workSessions = weekSessions.filter { it.type == "work" }
                val focusTime = workSessions.sumOf { it.duration }

                _uiState.value = _uiState.value.copy(
                    weekSessions = workSessions.size,
                    weekFocusTime = focusTime
                )
            }
        }
    }
}