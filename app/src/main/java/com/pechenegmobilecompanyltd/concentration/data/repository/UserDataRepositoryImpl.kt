package com.pechenegmobilecompanyltd.concentration.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.pechenegmobilecompanyltd.concentration.data.model.UserData
import com.pechenegmobilecompanyltd.concentration.data.remote.auth.FirebaseAuthDataSource
import com.pechenegmobilecompanyltd.concentration.domain.repository.UserDataRepository
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class UserDataRepositoryImpl : UserDataRepository, KoinComponent {
    private val firestore: FirebaseFirestore by inject()
    private val authDataSource: FirebaseAuthDataSource by inject()

    override suspend fun saveUserData(userData: UserData) {
        val userId = authDataSource.getCurrentUserId() ?: return
        val userDataWithId = userData.copy(userId = userId)

        firestore.collection("users")
            .document(userId)
            .set(userDataWithId)
            .await()
    }

    override suspend fun getUserData(): UserData {
        val userId = authDataSource.getCurrentUserId() ?: return UserData()

        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            document.toObject(UserData::class.java) ?: UserData(userId = userId)
        } catch (e: Exception) {
            UserData(userId = userId)
        }
    }

    override suspend fun updateSessionStats(sessionsToday: Int, focusTimeToday: Int) {
        val userId = authDataSource.getCurrentUserId() ?: return

        firestore.collection("users")
            .document(userId)
            .update(
                "totalSessions", FieldValue.increment(sessionsToday.toLong()),
                "totalFocusTime", FieldValue.increment(focusTimeToday.toLong()),
                "lastActivityDate", Date()
            )
            .await()
    }

    override suspend fun updateStreak(streak: Int) {
        val userId = authDataSource.getCurrentUserId() ?: return

        firestore.collection("users")
            .document(userId)
            .update("currentStreak", streak)
            .await()
    }

    override suspend fun updateBestDay(sessions: Int, date: Date) {
        val userId = authDataSource.getCurrentUserId() ?: return

        firestore.collection("users")
            .document(userId)
            .update(
                "bestDaySessions", sessions,
                "bestDayDate", date
            )
            .await()
    }
}