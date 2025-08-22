package com.pechenegmobilecompanyltd.concentration.data.model

import java.util.*

data class DailyStats(
    val date: Date,
    val completedSessions: Int,
    val totalFocusTime: Int, // in minutes
    val productivityScore: Float // 0.0 to 1.0
)

data class WeeklyStats(
    val weekStart: Date,
    val totalSessions: Int,
    val totalFocusTime: Int,
    val averageDailyScore: Float
)

data class ProductivityTrend(
    val dates: List<Date>,
    val scores: List<Float>
)