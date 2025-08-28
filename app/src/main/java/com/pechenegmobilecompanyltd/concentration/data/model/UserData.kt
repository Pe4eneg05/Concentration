package com.pechenegmobilecompanyltd.concentration.data.model

import java.util.*

data class UserData(
    val userId: String = "",
    val totalSessions: Int = 0,
    val totalFocusTime: Int = 0, // в минутах
    val bestDaySessions: Int = 0,
    val bestDayDate: Date? = null,
    val currentStreak: Int = 0,
    val lastActivityDate: Date = Date(),
    val selectedPreset: TimerPreset = TimerPreset("Стандартный", 25, 5)
) {
    // Конструктор для Firestore
    constructor() : this("", 0, 0, 0, null, 0, Date())
}