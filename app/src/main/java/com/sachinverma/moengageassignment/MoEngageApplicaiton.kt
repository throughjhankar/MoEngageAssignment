package com.sachinverma.moengageassignment

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MoEngageApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Using DI for providing application context
        startKoin{
            androidContext(applicationContext)
        }
        // Configuring Timber to show Timber logs in the logcat
        Timber.plant(Timber.DebugTree())
    }
}
