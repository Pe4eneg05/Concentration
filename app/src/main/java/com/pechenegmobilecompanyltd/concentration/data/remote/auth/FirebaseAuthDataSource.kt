package com.pechenegmobilecompanyltd.concentration.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FirebaseAuthDataSource : KoinComponent {
    private val auth: FirebaseAuth by inject()

    suspend fun signInAnonymously(): String {
        // Сначала проверяем, может пользователь уже есть
        val currentUser = auth.currentUser
        if (currentUser != null) {
            return currentUser.uid
        }

        // Если нет - создаем нового
        val result = auth.signInAnonymously().await()
        return result.user?.uid ?: throw Exception("Failed to sign in anonymously")
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}