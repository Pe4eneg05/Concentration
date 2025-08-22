package com.pechenegmobilecompanyltd.concentration.domain.repository

import com.pechenegmobilecompanyltd.concentration.data.model.DailyStats
import com.pechenegmobilecompanyltd.concentration.data.model.ProductivityTrend
import com.pechenegmobilecompanyltd.concentration.data.model.WeeklyStats
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    suspend fun getDailyStats(days: Int): List<DailyStats>
    suspend fun getWeeklyStats(weeks: Int): List<WeeklyStats>
    suspend fun getProductivityTrend(days: Int): ProductivityTrend
    suspend fun getBestDay(): DailyStats?
    suspend fun getCurrentStreak(): Int
}