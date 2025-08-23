package com.pechenegmobilecompanyltd.concentration.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pechenegmobilecompanyltd.concentration.data.model.TimerPreset
import com.pechenegmobilecompanyltd.concentration.data.remote.auth.FirebaseAuthDataSource
import com.pechenegmobilecompanyltd.concentration.domain.repository.UserSettingsRepository
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserSettingsRepositoryImpl : UserSettingsRepository, KoinComponent {
    private val firestore: FirebaseFirestore by inject()
    private val authDataSource: FirebaseAuthDataSource by inject()

    override suspend fun saveSelectedPreset(preset: TimerPreset) {
        try {
            val userId = authDataSource.getCurrentUserId() ?: return

            val data = hashMapOf(
                "selectedPreset" to hashMapOf(
                    "name" to preset.name,
                    "workDuration" to preset.workDuration,
                    "breakDuration" to preset.breakDuration
                )
            )

            firestore.collection("user_settings")
                .document(userId)
                .set(data)
                .await()
        } catch (e: Exception) {
            // Логируем ошибку, но не падаем
            println("Failed to save preset: ${e.message}")
        }
    }

    override suspend fun getSelectedPreset(): TimerPreset {
        return try {
            val userId = authDataSource.getCurrentUserId() ?: return TimerPreset("Стандартный", 25, 5)

            val document = firestore.collection("user_settings")
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val presetData = document.get("selectedPreset") as? Map<*, *>
                presetData?.let {
                    TimerPreset(
                        name = it["name"] as? String ?: "Стандартный",
                        workDuration = (it["workDuration"] as? Long)?.toInt() ?: 25,
                        breakDuration = (it["breakDuration"] as? Long)?.toInt() ?: 5
                    )
                } ?: TimerPreset("Стандартный", 25, 5)
            } else {
                TimerPreset("Стандартный", 25, 5)
            }
        } catch (e: Exception) {
            TimerPreset("Стандартный", 25, 5)
        }
    }
}