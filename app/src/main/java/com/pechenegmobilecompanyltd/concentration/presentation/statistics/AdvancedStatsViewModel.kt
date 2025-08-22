package com.pechenegmobilecompanyltd.concentration.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pechenegmobilecompanyltd.concentration.data.model.DailyStats
import com.pechenegmobilecompanyltd.concentration.data.model.ProductivityTrend
import com.pechenegmobilecompanyltd.concentration.data.model.WeeklyStats
import com.pechenegmobilecompanyltd.concentration.domain.repository.StatsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class AdvancedStatsState(
    val dailyStats: List<DailyStats> = emptyList(),
    val weeklyStats: List<WeeklyStats> = emptyList(),
    val productivityTrend: ProductivityTrend = ProductivityTrend(emptyList(), emptyList()),
    val bestDay: DailyStats? = null,
    val currentStreak: Int = 0
)

class AdvancedStatsViewModel : ViewModel(), KoinComponent {
    private val statsRepository: StatsRepository by inject()

    private val _uiState = MutableStateFlow(AdvancedStatsState())
    val uiState: StateFlow<AdvancedStatsState> = _uiState

    fun loadAdvancedStats() {
        viewModelScope.launch {
            val dailyStats = statsRepository.getDailyStats(30)
            val weeklyStats = statsRepository.getWeeklyStats(8)
            val productivityTrend = statsRepository.getProductivityTrend(14)
            val bestDay = statsRepository.getBestDay()
            val currentStreak = statsRepository.getCurrentStreak()

            _uiState.value = AdvancedStatsState(
                dailyStats = dailyStats,
                weeklyStats = weeklyStats,
                productivityTrend = productivityTrend,
                bestDay = bestDay,
                currentStreak = currentStreak
            )
        }
    }
}