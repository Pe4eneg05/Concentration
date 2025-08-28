package com.pechenegmobilecompanyltd.concentration.domain.repository

import com.pechenegmobilecompanyltd.concentration.data.model.UserData
import java.util.Date

interface UserDataRepository {
    suspend fun saveUserData(userData: UserData)
    suspend fun getUserData(): UserData
    suspend fun updateSessionStats(sessionsToday: Int, focusTimeToday: Int)
    suspend fun updateStreak(streak: Int)
    suspend fun updateBestDay(sessions: Int, date: Date)
}