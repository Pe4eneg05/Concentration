package com.pechenegmobilecompanyltd.concentration.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatScreen(
    viewModel: StatViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAdvancedStats: () -> Unit // Добавляем навигацию к расширенной статистике
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Статистика") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "Ваша статистика",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                StatCard(
                    title = "Сегодня",
                    sessions = state.todaySessions,
                    focusTime = state.todayFocusTime
                )
            }

            item {
                StatCard(
                    title = "За неделю",
                    sessions = state.weekSessions,
                    focusTime = state.weekFocusTime
                )
            }

            item {
                // Здесь позже добавим график
                Text(
                    text = "График продуктивности",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onNavigateToAdvancedStats,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Подробная статистика")
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, sessions: Int, focusTime: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$sessions помидорок")
            Text(text = "$focusTime минут фокуса")
        }
    }
}