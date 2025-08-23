package com.pechenegmobilecompanyltd.concentration.data.repository

import com.pechenegmobilecompanyltd.concentration.data.model.TimerPreset
import com.pechenegmobilecompanyltd.concentration.domain.repository.PresetRepository

class PresetRepositoryImpl : PresetRepository {
    override fun getPresets(): List<TimerPreset> {
        return listOf(
            TimerPreset("Стандартный", 25, 5),
            TimerPreset("Короткий", 15, 3),
            TimerPreset("Длинный", 45, 10),
            TimerPreset("Ультра-короткий", 5, 1)
        )
    }

    override fun getDefaultPreset(): TimerPreset {
        return getPresets().first()
    }
}