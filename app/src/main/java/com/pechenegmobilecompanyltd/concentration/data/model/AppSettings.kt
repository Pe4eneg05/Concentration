package com.pechenegmobilecompanyltd.concentration.data.model

data class AppSettings(
    val workDuration: Int = 25,
    val shortBreakDuration: Int = 5,
    val longBreakDuration: Int = 15,
    val sessionsBeforeLongBreak: Int = 4,
    val autoStartNextSession: Boolean = false
)