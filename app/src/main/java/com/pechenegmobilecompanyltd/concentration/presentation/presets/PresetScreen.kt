package com.pechenegmobilecompanyltd.concentration.presentation.presets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pechenegmobilecompanyltd.concentration.data.model.TimerPreset
import com.pechenegmobilecompanyltd.concentration.presentation.main.TimerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetScreen(
    viewModel: PresetViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val presets = viewModel.getPresets()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Выбор режима") },
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
            items(presets.size) { index ->
                PresetCard(
                    preset = presets[index],
                    onSelect = {
                        viewModel.applyPreset(presets[index]) // Используем viewModel
                        onNavigateBack()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PresetCard(preset: TimerPreset, onSelect: () -> Unit) {
    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = preset.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Работа: ${preset.workDuration} мин / Отдых: ${preset.breakDuration} мин",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}