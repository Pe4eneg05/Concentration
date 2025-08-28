package com.pechenegmobilecompanyltd.concentration

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pechenegmobilecompanyltd.concentration.presentation.navigation.AppNavigation
import com.pechenegmobilecompanyltd.concentration.presentation.profile.ProfileViewModel
import com.pechenegmobilecompanyltd.concentration.ui.theme.FocusConcentrationTheme

class MainActivity : ComponentActivity() {
    private var signInIntentData: Intent? by mutableStateOf(null)

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        signInIntentData = result.data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocusConcentrationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ProfileViewModel = viewModel()

                    // Обработка результата входа
                    LaunchedEffect(signInIntentData) {
                        signInIntentData?.let { data ->
                            viewModel.handleSignInResult(data)
                            signInIntentData = null
                        }
                    }

                    // Обработка запроса на вход
                    LaunchedEffect(viewModel.uiState.value.signInIntent) {
                        viewModel.uiState.value.signInIntent?.let { intent ->
                            googleSignInLauncher.launch(intent)
                            viewModel.clearSignInIntent()
                        }
                    }

                    AppNavigation()
                }
            }
        }
    }
}