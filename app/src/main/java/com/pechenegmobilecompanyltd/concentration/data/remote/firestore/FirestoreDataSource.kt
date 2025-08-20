package com.pechenegmobilecompanyltd.concentration.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.pechenegmobilecompanyltd.concentration.data.model.Session
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FirestoreDataSource : KoinComponent {
    private val firestore: FirebaseFirestore by inject()

    suspend fun saveSession(session: Session, userId: String) {
        val sessionWithUserId = session.copy(userId = userId)
        firestore.collection("sessions")
            .add(sessionWithUserId)
            .await()
    }

    suspend fun getSessions(userId: String): List<Session> {
        return firestore.collection("sessions")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject<Session>() }
    }
}