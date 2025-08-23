package com.pechenegmobilecompanyltd.concentration.presentation.presets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pechenegmobilecompanyltd.concentration.data.manager.PresetManager
import com.pechenegmobilecompanyltd.concentration.data.model.TimerPreset
import com.pechenegmobilecompanyltd.concentration.domain.repository.PresetRepository
import com.pechenegmobilecompanyltd.concentration.domain.repository.UserSettingsRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PresetViewModel : ViewModel(), KoinComponent {
    private val presetRepository: PresetRepository by inject()
    private val userSettingsRepository: UserSettingsRepository by inject()

    fun getPresets() = presetRepository.getPresets()

    fun applyPreset(preset: TimerPreset) {
        viewModelScope.launch {
            userSettingsRepository.saveSelectedPreset(preset)
            PresetManager.updatePreset(preset) // Уведомляем всех о изменении
        }
    }
}