package com.pechenegmobilecompanyltd.concentration.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pechenegmobilecompanyltd.concentration.data.repository.SettingsRepositoryImpl
import com.pechenegmobilecompanyltd.concentration.data.remote.auth.FirebaseAuthDataSource
import com.pechenegmobilecompanyltd.concentration.data.remote.firestore.FirestoreDataSource
import com.pechenegmobilecompanyltd.concentration.data.repository.SessionRepositoryImpl
import com.pechenegmobilecompanyltd.concentration.data.repository.StatsRepositoryImpl
import com.pechenegmobilecompanyltd.concentration.domain.repository.SessionRepository
import com.pechenegmobilecompanyltd.concentration.domain.repository.SettingsRepository
import com.pechenegmobilecompanyltd.concentration.domain.repository.StatsRepository
import com.pechenegmobilecompanyltd.concentration.presentation.main.TimerViewModel
import com.pechenegmobilecompanyltd.concentration.presentation.settings.SettingsViewModel
import com.pechenegmobilecompanyltd.concentration.presentation.statistics.AdvancedStatsViewModel
import com.pechenegmobilecompanyltd.concentration.presentation.statistics.StatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Создаем DataStore делегат
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {
    // DataStore
    single<DataStore<Preferences>> { androidContext().dataStore }

    // Firebase services
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // DataSources
    single { FirestoreDataSource() }
    single { FirebaseAuthDataSource() }

    // Repositories
    single<SessionRepository> { SessionRepositoryImpl() }
    single<SettingsRepository> { SettingsRepositoryImpl() }
    single<StatsRepository> { StatsRepositoryImpl() }

    // ViewModels
    viewModel { TimerViewModel() }
    viewModel { StatViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { AdvancedStatsViewModel() }
}