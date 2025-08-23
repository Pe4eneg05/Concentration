package com.pechenegmobilecompanyltd.concentration.domain.repository

import com.pechenegmobilecompanyltd.concentration.data.model.TimerPreset

interface PresetRepository {
    fun getPresets(): List<TimerPreset>
    fun getDefaultPreset(): TimerPreset
}