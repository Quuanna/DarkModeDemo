package com.anna.darkmode

import android.app.Application
import com.anna.darkmode.manager.SharedPreferencesManager

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.getInstance(this)
    }
}