package com.pechenegmobilecompanyltd.concentration.data.repository

import com.pechenegmobilecompanyltd.concentration.data.model.Session
import com.pechenegmobilecompanyltd.concentration.data.remote.auth.FirebaseAuthDataSource
import com.pechenegmobilecompanyltd.concentration.data.remote.firestore.FirestoreDataSource
import com.pechenegmobilecompanyltd.concentration.domain.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SessionRepositoryImpl : SessionRepository, KoinComponent {
    private val authDataSource: FirebaseAuthDataSource by inject()
    private val firestoreDataSource: FirestoreDataSource by inject()

    private var cachedUserId: String? = null
    private val mutex = Mutex() // Защита от race condition

    private suspend fun getUserId(): String = mutex.withLock {
        // Если уже есть кешированный ID - возвращаем его
        if (cachedUserId != null) {
            return cachedUserId!!
        }

        // Пытаемся получить существующего пользователя
        val existingUserId = authDataSource.getCurrentUserId()
        if (existingUserId != null) {
            cachedUserId = existingUserId
            return existingUserId
        }

        // Если пользователя нет - создаем нового (ТОЛЬКО ОДИН РАЗ!)
        val newUserId = authDataSource.signInAnonymously()
        cachedUserId = newUserId
        return newUserId
    }

    override suspend fun saveSession(session: Session): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val userId = getUserId()
            firestoreDataSource.saveSession(session, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSessions(): Result<List<Session>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val userId = getUserId()
            val sessions = firestoreDataSource.getSessions(userId)
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTodaySessions(): Result<List<Session>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val userId = getUserId()
            val allSessions = firestoreDataSource.getSessions(userId)
            val today = java.util.Date()
            val todayStart = java.util.Calendar.getInstance().apply {
                time = today
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }.time

            val todaySessions = allSessions.filter { session ->
                session.startTime.after(todayStart) || session.startTime == todayStart
            }
            Result.success(todaySessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}