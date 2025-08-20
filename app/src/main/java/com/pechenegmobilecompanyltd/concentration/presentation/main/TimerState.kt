package com.pechenegmobilecompanyltd.concentration.presentation.main

sealed class TimerPhase {
    object Work : TimerPhase()
    object Break : TimerPhase()
}

data class TimerState(
    val progress: Float = 1f,
    val currentTime: String = "25:00",
    val isRunning: Boolean = false,
    val currentPhase: TimerPhase = TimerPhase.Work,
    val totalSessionsToday: Int = 0,
    val totalFocusTimeToday: Int = 0 // в минутах
)