package com.pechenegmobilecompanyltd.concentration.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pechenegmobilecompanyltd.concentration.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class ProfileState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val signInIntent: android.content.Intent? = null // Добавляем поле для интента
)

class ProfileViewModel : ViewModel(), KoinComponent {
    private val authRepository: AuthRepository by inject()

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    init {
        checkAuthStatus()
    }

    fun prepareGoogleSignIn() {
        _uiState.value = _uiState.value.copy(
            signInIntent = authRepository.getGoogleSignInIntent(),
            error = null
        )
    }

    fun handleSignInResult(data: android.content.Intent?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = authRepository.handleSignInResult(data)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isAuthenticated = true,
                        isLoading = false,
                        signInIntent = null // Сбрасываем интент после успешного входа
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = result.exceptionOrNull()?.message ?: "Ошибка авторизации",
                        isLoading = false,
                        signInIntent = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Ошибка авторизации",
                    isLoading = false,
                    signInIntent = null
                )
            }
        }
    }

    fun clearSignInIntent() {
        _uiState.value = _uiState.value.copy(signInIntent = null)
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.value = _uiState.value.copy(isAuthenticated = false)
        }
    }

    private fun checkAuthStatus() {
        _uiState.value = _uiState.value.copy(
            isAuthenticated = authRepository.isUserAuthenticated()
        )
    }
}