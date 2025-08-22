package com.pechenegmobilecompanyltd.concentration.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: TimerViewModel = koinViewModel(),
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit // Добавляем навигацию в настройки
)  {
    val state by viewModel.timerState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTodayStats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Концентрация и Фокус") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Настройки"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Круговой прогресс-бар с таймером
            CircularTimer(
                progress = state.progress,
                timeText = state.currentTime,
                phase = state.currentPhase,
                size = 300.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки управления
            TimerControls(
                isRunning = state.isRunning,
                currentPhase = state.currentPhase,
                onStart = { viewModel.startTimer() },
                onPause = { viewModel.pauseTimer() },
                onReset = { viewModel.resetTimer() },
                onSkipBreak = { viewModel.skipBreak() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Статистика за сегодня
            TodayStats(
                sessionsCount = state.totalSessionsToday,
                focusTime = state.totalFocusTimeToday
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка перехода к статистике
            Button(
                onClick = onNavigateToStats,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Статистика")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка перехода к настройкам
            Button(
                onClick = onNavigateToSettings,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Настройки")
            }
        }
    }
}

@Composable
fun CircularTimer(
    progress: Float,
    timeText: String,
    phase: TimerPhase,
    size: Dp
) {
    val progressColor = when (phase) {
        is TimerPhase.Work -> Color(0xFFFF6B6B) // Красный для работы
        is TimerPhase.Break -> Color(0xFF4ECDC4) // Бирюзовый для отдыха
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        // Фоновый круг
        Canvas(modifier = Modifier.size(size)) {
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.3f),
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Прогресс-бар
        Canvas(modifier = Modifier.size(size)) {
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Текст времени
        Text(
            text = timeText,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Текст фазы
        Text(
            text = when (phase) {
                is TimerPhase.Work -> "Работа"
                is TimerPhase.Break -> "Отдых"
            },
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 80.dp)
        )
    }
}

@Composable
fun TimerControls(
    isRunning: Boolean,
    currentPhase: TimerPhase,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSkipBreak: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Основные кнопки управления
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isRunning) {
                Button(
                    onClick = onPause,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text("Пауза")
                }
            } else {
                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4ECDC4)
                    )
                ) {
                    Text("Старт")
                }
            }

            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                )
            ) {
                Text("Сброс")
            }
        }

        // Кнопка пропуска перерыва (только во время отдыха)
        if (currentPhase is TimerPhase.Break) {
            Button(
                onClick = onSkipBreak,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFE66D)
                )
            ) {
                Text("Пропустить перерыв")
            }
        }
    }
}

@Composable
fun TodayStats(
    sessionsCount: Int,
    focusTime: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Сегодня",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$sessionsCount помидорок",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Text(
            text = "${focusTime} минут фокуса",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}