package com.pechenegmobilecompanyltd.concentration.domain.repository

import com.pechenegmobilecompanyltd.concentration.data.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveSettings(settings: AppSettings)
    fun getSettings(): Flow<AppSettings>
}