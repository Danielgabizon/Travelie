package org.colman.travelie

import android.app.Application
import org.colman.travelie.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger


class MyApplication: Application() { // Application class runs before the first activity is created
    // Great place to initialize global stuff — like Koin.

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }
    }
}