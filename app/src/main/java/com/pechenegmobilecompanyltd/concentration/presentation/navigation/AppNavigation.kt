package com.pechenegmobilecompanyltd.concentration.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pechenegmobilecompanyltd.concentration.presentation.main.TimerScreen
import com.pechenegmobilecompanyltd.concentration.presentation.presets.PresetScreen
import com.pechenegmobilecompanyltd.concentration.presentation.profile.ProfileScreen
import com.pechenegmobilecompanyltd.concentration.presentation.settings.SettingsScreen
import com.pechenegmobilecompanyltd.concentration.presentation.statistics.AdvancedStatsScreen
import com.pechenegmobilecompanyltd.concentration.presentation.statistics.StatScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Timer.route
    ) {
        composable(Screen.Timer.route) {
            TimerScreen(
                onNavigateToStats = { navController.navigate(Screen.Statistics.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onShowPresets = { navController.navigate(Screen.Presets.route) }
            )
        }

        composable(Screen.Statistics.route) {
            StatScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAdvancedStats = { navController.navigate(Screen.AdvancedStatistics.route) }
            )
        }

        composable(Screen.AdvancedStatistics.route) {
            AdvancedStatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Presets.route) {
            PresetScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// Обновляем класс экранов
sealed class Screen(val route: String) {
    object Timer : Screen("timer")
    object Profile : Screen("profile")
    object Statistics : Screen("statistics")
    object AdvancedStatistics : Screen("advanced_statistics")
    object Settings : Screen("settings")
    object Presets : Screen("presets") // Добавляем экран пресетов
}