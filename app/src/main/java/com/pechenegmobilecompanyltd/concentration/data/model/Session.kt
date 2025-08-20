package com.pechenegmobilecompanyltd.concentration.data.model

import java.util.Date

data class Session(
    val id: String = "", // Будет заполнено автоматически Firestore
    val userId: String = "", // Важно для безопасности!
    val startTime: Date = Date(),
    val endTime: Date = Date(),
    val duration: Int = 0, // в минутах
    val type: String = "" // "work" или "break"
)