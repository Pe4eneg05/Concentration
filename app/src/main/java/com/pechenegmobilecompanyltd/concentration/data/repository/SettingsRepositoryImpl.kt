package com.pechenegmobilecompanyltd.concentration.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.pechenegmobilecompanyltd.concentration.data.model.AppSettings
import com.pechenegmobilecompanyltd.concentration.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsRepositoryImpl : SettingsRepository, KoinComponent {
    private val dataStore: DataStore<Preferences> by inject()

    companion object {
        private val WORK_DURATION = intPreferencesKey("work_duration")
        private val SHORT_BREAK_DURATION = intPreferencesKey("short_break_duration")
        private val LONG_BREAK_DURATION = intPreferencesKey("long_break_duration")
        private val SESSIONS_BEFORE_LONG_BREAK = intPreferencesKey("sessions_before_long_break")
        private val AUTO_START_NEXT_SESSION = booleanPreferencesKey("auto_start_next_session")
    }

    override suspend fun saveSettings(settings: AppSettings) {
        dataStore.edit { preferences ->
            preferences[WORK_DURATION] = settings.workDuration
            preferences[SHORT_BREAK_DURATION] = settings.shortBreakDuration
            preferences[LONG_BREAK_DURATION] = settings.longBreakDuration
            preferences[SESSIONS_BEFORE_LONG_BREAK] = settings.sessionsBeforeLongBreak
            preferences[AUTO_START_NEXT_SESSION] = settings.autoStartNextSession
        }
    }

    override fun getSettings(): Flow<AppSettings> {
        return dataStore.data.map { preferences ->
            AppSettings(
                workDuration = preferences[WORK_DURATION] ?: 25,
                shortBreakDuration = preferences[SHORT_BREAK_DURATION] ?: 5,
                longBreakDuration = preferences[LONG_BREAK_DURATION] ?: 15,
                sessionsBeforeLongBreak = preferences[SESSIONS_BEFORE_LONG_BREAK] ?: 4,
                autoStartNextSession = preferences[AUTO_START_NEXT_SESSION] ?: false
            )
        }
    }
}