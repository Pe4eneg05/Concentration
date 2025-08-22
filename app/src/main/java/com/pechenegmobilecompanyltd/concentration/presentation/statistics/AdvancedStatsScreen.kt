package com.pechenegmobilecompanyltd.concentration.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pechenegmobilecompanyltd.concentration.data.model.DailyStats
import com.pechenegmobilecompanyltd.concentration.presentation.components.ProductivityChart
import org.koin.androidx.compose.koinViewModel
import java.util.Date
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedStatsScreen(
    viewModel: AdvancedStatsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAdvancedStats()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Детальная статистика") },
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
                // График продуктивности
                ProductivityChart(
                    data = state.productivityTrend,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Ежедневная статистика",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            state.dailyStats.take(7).forEach { dailyStat ->
                item {
                    DailyStatCard(dailyStat = dailyStat)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Рекорды",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                RecordCard(
                    title = "Лучший день",
                    value = state.bestDay?.completedSessions?.toString() ?: "0",
                    subtitle = "сессий"
                )
                Spacer(modifier = Modifier.height(8.dp))
                RecordCard(
                    title = "Текущая серия",
                    value = state.currentStreak.toString(),
                    subtitle = "дней подряд"
                )
            }
        }
    }
}

@Composable
fun DailyStatCard(dailyStat: DailyStats) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = formatDate(dailyStat.date),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${dailyStat.completedSessions} сессий")
            Text(text = "${dailyStat.totalFocusTime} минут")
            LinearProgressIndicator(
                progress = { dailyStat.productivityScore },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun RecordCard(title: String, value: String, subtitle: String) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// Вспомогательная функция для форматирования даты
fun formatDate(date: Date): String {
    val calendar = Calendar.getInstance().apply { time = date }
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return when {
        calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Сегодня"

        calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) - 1 -> "Вчера"

        else -> {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            "${day.toString().padStart(2, '0')}.${month.toString().padStart(2, '0')}"
        }
    }
}