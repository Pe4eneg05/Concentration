package com.pechenegmobilecompanyltd.concentration.data.manager

import com.pechenegmobilecompanyltd.concentration.data.model.TimerPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PresetManager {
    private val _currentPreset = MutableStateFlow(TimerPreset("Стандартный", 25, 5))
    val currentPreset: StateFlow<TimerPreset> = _currentPreset

    fun updatePreset(preset: TimerPreset) {
        _currentPreset.value = preset
    }
}