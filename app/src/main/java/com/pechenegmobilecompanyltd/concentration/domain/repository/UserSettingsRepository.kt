package com.pechenegmobilecompanyltd.concentration.domain.repository

import com.pechenegmobilecompanyltd.concentration.data.model.TimerPreset

interface UserSettingsRepository {
    suspend fun saveSelectedPreset(preset: TimerPreset)
    suspend fun getSelectedPreset(): TimerPreset
}