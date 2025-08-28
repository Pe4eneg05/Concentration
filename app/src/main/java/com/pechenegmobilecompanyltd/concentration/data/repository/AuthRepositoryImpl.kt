package com.pechenegmobilecompanyltd.concentration.data.repository

import android.app.Application
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pechenegmobilecompanyltd.concentration.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Application
) : AuthRepository, KoinComponent {

    private val auth: FirebaseAuth by inject()

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("746991647309-41uu31niinvnl4ja8ilep1uol1c8v65o.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    override fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    override suspend fun handleSignInResult(data: Intent?): Result<String> {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = auth.signInWithCredential(credential).await()

            Result.success(authResult.user?.uid ?: throw Exception("User ID is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(): Result<String> {
        return try {
            // Пытаемся использовать silent sign-in если возможно
            val account = googleSignInClient.silentSignIn().await()
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = auth.signInWithCredential(credential).await()

            Result.success(authResult.user?.uid ?: throw Exception("User ID is null"))
        } catch (e: Exception) {
            // Если silent sign-in не удался, возвращаем ошибку
            Result.failure(Exception("Требуется интерактивный вход"))
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        googleSignInClient.signOut().await()
    }

    override fun getCurrentUser(): String? {
        return auth.currentUser?.uid
    }

    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }
}