package com.pechenegmobilecompanyltd.concentration.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pechenegmobilecompanyltd.concentration.data.model.Session
import com.pechenegmobilecompanyltd.concentration.data.remote.auth.FirebaseAuthDataSource
import com.pechenegmobilecompanyltd.concentration.domain.repository.SessionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date
import kotlin.time.Duration.Companion.seconds

class TimerViewModel : ViewModel(), KoinComponent {
    private val repository: SessionRepository by inject()
    private val authDataSource: FirebaseAuthDataSource by inject()

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null
    private var totalSeconds = 25 * 60
    private var remainingSeconds = totalSeconds

    init {
        // Принудительно проверяем авторизацию при создании ViewModel
        viewModelScope.launch {
            try {
                // Просто проверяем, что пользователь есть
                authDataSource.getCurrentUserId()
                loadTodayStats()
            } catch (e: Exception) {
                // Игнорируем ошибки - авторизация произойдет при первом сохранении
                loadTodayStats()
            }
        }
    }

    fun startTimer() {
        _timerState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (remainingSeconds > 0 && _timerState.value.isRunning) {
                delay(1.seconds)
                remainingSeconds--
                updateTimerState()
            }

            if (remainingSeconds <= 0) {
                completeSession()
            }
        }
    }

    fun pauseTimer() {
        _timerState.update { it.copy(isRunning = false) }
        timerJob?.cancel()
    }

    fun resetTimer() {
        timerJob?.cancel()
        remainingSeconds = totalSeconds
        _timerState.update {
            it.copy(
                isRunning = false,
                progress = 1f,
                currentTime = formatTime(remainingSeconds)
            )
        }
    }

    fun skipBreak() {
        if (_timerState.value.currentPhase is TimerPhase.Break) {
            switchToWorkPhase()
        }
    }

    private fun updateTimerState() {
        val progress = remainingSeconds.toFloat() / totalSeconds.toFloat()
        _timerState.update {
            it.copy(
                progress = progress,
                currentTime = formatTime(remainingSeconds)
            )
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    private fun completeSession() {
        viewModelScope.launch {
            val currentPhase = _timerState.value.currentPhase

            if (currentPhase is TimerPhase.Work) {
                try {
                    // Сохраняем рабочую сессию
                    val session = Session(
                        startTime = Date(System.currentTimeMillis() - totalSeconds * 1000L),
                        endTime = Date(),
                        duration = totalSeconds / 60,
                        type = "work"
                    )
                    val result = repository.saveSession(session)

                    result.onSuccess {
                        Log.d("TimerDebug", "Session saved successfully!")
                        // Переключаемся на перерыв
                        switchToBreakPhase()
                        loadTodayStats() // Обновляем статистику
                    }.onFailure { error ->
                        Log.e("TimerDebug", "Failed to save session: ${error.message}")
                        // Все равно переключаемся, но показываем ошибку?
                        switchToBreakPhase()
                    }
                } catch (e: Exception) {
                    Log.e("TimerDebug", "Exception in completeSession: ${e.message}")
                    switchToBreakPhase()
                }
            } else {
                // Перерыв завершен, возвращаемся к работе
                switchToWorkPhase()
            }
        }
    }

    private fun switchToWorkPhase() {
        totalSeconds = 25 * 60
        remainingSeconds = totalSeconds
        _timerState.update {
            it.copy(
                currentPhase = TimerPhase.Work,
                progress = 1f,
                currentTime = formatTime(remainingSeconds),
                isRunning = false
            )
        }
    }

    private fun switchToBreakPhase() {
        totalSeconds = 5 * 60
        remainingSeconds = totalSeconds
        _timerState.update {
            it.copy(
                currentPhase = TimerPhase.Break,
                progress = 1f,
                currentTime = formatTime(remainingSeconds),
                isRunning = false
            )
        }
    }

    fun loadTodayStats() {
        viewModelScope.launch {
            val result = repository.getTodaySessions()
            result.onSuccess { sessions ->
                val workSessions = sessions.filter { it.type == "work" }
                val totalFocusTime = workSessions.sumOf { it.duration }

                _timerState.update {
                    it.copy(
                        totalSessionsToday = workSessions.size,
                        totalFocusTimeToday = totalFocusTime
                    )
                }
            }
        }
    }
}