package com.pechenegmobilecompanyltd.concentration

import android.app.Application
import com.pechenegmobilecompanyltd.concentration.di.appModule
import com.pechenegmobilecompanyltd.concentration.di.firebaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FocusApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FocusApp)
            modules(appModule, firebaseModule)
        }
    }
}