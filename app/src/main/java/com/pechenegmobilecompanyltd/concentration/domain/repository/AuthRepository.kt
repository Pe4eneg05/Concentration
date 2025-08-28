package com.pechenegmobilecompanyltd.concentration.domain.repository

import android.content.Intent

interface AuthRepository {
    suspend fun signInWithGoogle(): Result<String>
    suspend fun signOut()
    fun getCurrentUser(): String?
    fun isUserAuthenticated(): Boolean
    fun getGoogleSignInIntent(): Intent
    suspend fun handleSignInResult(data: Intent?): Result<String>
}