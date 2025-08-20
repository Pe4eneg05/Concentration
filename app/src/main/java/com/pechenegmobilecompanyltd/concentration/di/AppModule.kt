package com.pechenegmobilecompanyltd.concentration.di

import com.pechenegmobilecompanyltd.concentration.data.remote.auth.FirebaseAuthDataSource
import com.pechenegmobilecompanyltd.concentration.data.remote.firestore.FirestoreDataSource
import com.pechenegmobilecompanyltd.concentration.data.repository.SessionRepositoryImpl
import com.pechenegmobilecompanyltd.concentration.domain.repository.SessionRepository
import com.pechenegmobilecompanyltd.concentration.presentation.main.TimerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // DataSources
    single { FirestoreDataSource() }
    single { FirebaseAuthDataSource() }

    // Repository
    single<SessionRepository> { SessionRepositoryImpl() }

    // ViewModels
    viewModel { TimerViewModel() }
}