package com.example.albertsontask.application

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlbertsonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}