package com.example.myanimelistapp

import android.app.Application
import timber.log.Timber

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.d("Data item berhasil dimuat: ${sampleItems.size} item")
    }
}
