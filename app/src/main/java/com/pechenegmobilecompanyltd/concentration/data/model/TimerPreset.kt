package com.pechenegmobilecompanyltd.concentration.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimerPreset(
    val name: String,
    val workDuration: Int, // в минутах
    val breakDuration: Int // в минутах
) : Parcelable