package com.anna.darkmode.manager

import android.content.Context

class SharedPreferencesManager private constructor(context: Context) {

    private val sharedPref = context.getSharedPreferences("darkMode", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: SharedPreferencesManager? = null
        private const val DARK_MODE = "DARK_MODE"

        fun getInstance(context: Context): SharedPreferencesManager {
            if (instance == null) {
                synchronized(SharedPreferencesManager::class.java) {
                    if (instance == null) {
                        instance = SharedPreferencesManager(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }


    fun saveDarkMode(isDarkMode: Boolean) {
        val editor = sharedPref?.edit()
        editor?.putBoolean(DARK_MODE, isDarkMode)
        editor?.apply()
    }

    fun isDarkMode(): Boolean {
        return sharedPref.getBoolean(DARK_MODE, false) // 預設為淺色模式
    }
}