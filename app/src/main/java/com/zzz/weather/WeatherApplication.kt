package com.zzz.weather

import android.app.Application
import android.content.Context

class WeatherApplication: Application() {

    companion object {
        lateinit var context: Context

        const val TOKEN = "auv03EiB3Zt9RZcn"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}