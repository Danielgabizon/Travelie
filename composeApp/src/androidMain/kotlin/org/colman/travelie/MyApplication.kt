package org.colman.travelie

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import org.colman.travelie.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger


class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }
    }
}