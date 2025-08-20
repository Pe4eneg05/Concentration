package com.pechenegmobilecompanyltd.concentration.domain.repository

import com.pechenegmobilecompanyltd.concentration.data.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun saveSession(session: Session): Result<Unit>
    suspend fun getSessions(): Result<List<Session>>
    suspend fun getTodaySessions(): Result<List<Session>>
}