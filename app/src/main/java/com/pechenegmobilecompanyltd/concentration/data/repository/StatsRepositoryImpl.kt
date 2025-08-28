package com.pechenegmobilecompanyltd.concentration.data.repository

import com.pechenegmobilecompanyltd.concentration.data.model.DailyStats
import com.pechenegmobilecompanyltd.concentration.data.model.ProductivityTrend
import com.pechenegmobilecompanyltd.concentration.data.model.WeeklyStats
import com.pechenegmobilecompanyltd.concentration.domain.repository.SessionRepository
import com.pechenegmobilecompanyltd.concentration.domain.repository.StatsRepository
import com.pechenegmobilecompanyltd.concentration.domain.repository.UserDataRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.math.max

class StatsRepositoryImpl : StatsRepository, KoinComponent {
    private val sessionRepository: SessionRepository by inject()
    private val userDataRepository: UserDataRepository by inject()

    override suspend fun getDailyStats(days: Int): List<DailyStats> {
        val allSessions = sessionRepository.getSessions().getOrElse { return emptyList() }

        val calendar = Calendar.getInstance()
        val result = mutableListOf<DailyStats>()

        for (i in 0 until days) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val dayStart = calendar.time

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val dayEnd = calendar.time

            val daySessions = allSessions.filter { session ->
                session.startTime.time >= dayStart.time && session.startTime.time <= dayEnd.time
            }

            val workSessions = daySessions.filter { it.type == "work" }
            val completedSessions = workSessions.size
            val totalFocusTime = workSessions.sumOf { it.duration }

            val productivityScore = if (completedSessions > 0) {
                max(0.0, minOf(1.0, completedSessions / 10.0)).toFloat()
            } else {
                0f
            }

            result.add(
                DailyStats(
                    date = dayStart,
                    completedSessions = completedSessions,
                    totalFocusTime = totalFocusTime,
                    productivityScore = productivityScore
                )
            )
        }

        return result.sortedBy { it.date }
    }

    override suspend fun getWeeklyStats(weeks: Int): List<WeeklyStats> {
        val dailyStats = getDailyStats(weeks * 7)
        val result = mutableListOf<WeeklyStats>()

        val calendar = Calendar.getInstance()

        for (i in 0 until weeks) {
            calendar.time = Date()
            calendar.add(Calendar.WEEK_OF_YEAR, -i)
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val weekStart = calendar.time

            calendar.add(Calendar.DAY_OF_YEAR, 6)
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val weekEnd = calendar.time

            val weekDailyStats = dailyStats.filter {
                it.date.time >= weekStart.time && it.date.time <= weekEnd.time
            }

            val totalSessions = weekDailyStats.sumOf { it.completedSessions }
            val totalFocusTime = weekDailyStats.sumOf { it.totalFocusTime }
            val averageDailyScore = if (weekDailyStats.isNotEmpty()) {
                weekDailyStats.map { it.productivityScore }.average().toFloat()
            } else {
                0f
            }

            result.add(
                WeeklyStats(
                    weekStart = weekStart,
                    totalSessions = totalSessions,
                    totalFocusTime = totalFocusTime,
                    averageDailyScore = averageDailyScore
                )
            )
        }

        return result.sortedBy { it.weekStart }
    }

    override suspend fun getProductivityTrend(days: Int): ProductivityTrend {
        val dailyStats = getDailyStats(days)
        return ProductivityTrend(
            dates = dailyStats.map { it.date },
            scores = dailyStats.map { it.productivityScore }
        )
    }

    override suspend fun getBestDay(): DailyStats? {
        val userData = userDataRepository.getUserData()
        return userData.bestDayDate?.let { date ->
            DailyStats(
                date = date,
                completedSessions = userData.bestDaySessions,
                totalFocusTime = 0,
                productivityScore = 1.0f
            )
        }
    }

    override suspend fun getCurrentStreak(): Int {
        return userDataRepository.getUserData().currentStreak
    }

    override suspend fun getTotalSessions(): Int {
        return userDataRepository.getUserData().totalSessions
    }

    override suspend fun getTotalFocusTime(): Int {
        return userDataRepository.getUserData().totalFocusTime
    }
}