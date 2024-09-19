package com.anna.darkmode.manager

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.anna.darkmode.R
import java.lang.ref.WeakReference


class DarkModeManager private constructor(private val activity: WeakReference<FragmentActivity>) {

    private val uiModeManager =
        activity.get()?.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager


    companion object {
        @Volatile
        private var instance: DarkModeManager? = null

        fun getInstance(activity: FragmentActivity): DarkModeManager {
            if (instance == null) {
                synchronized(SharedPreferencesManager::class.java) {
                    if (instance == null) {
                        instance = DarkModeManager(WeakReference(activity))
                    }
                }
            }
            return instance!!
        }
    }


    /**
     *  Activity 頁面用
     */
    fun setDarkModeForActivity() {
        checkModeState { isDarkMode ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setDarkModeForApi31Above(isDarkMode, uiModeManager)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    setDarkModeForApi28ThroughApi30(isDarkMode)
                } else {
                    setDarkModeForApi27Below(isDarkMode, uiModeManager)
                }
            }
        }
    }

    /**
     * Fragment 頁面用
     */
    fun setDarkModeForFragment() {
        activity.get()?.supportFragmentManager?.fragments?.let {
            checkModeState { isDarkMode ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    setDarkModeForApi31Above(isDarkMode, uiModeManager)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        setDarkModeForApi28ThroughApi30(isDarkMode)
                    } else {
                        setDarkModeForApi27Below(isDarkMode, uiModeManager)
                    }
                }
            }
        }
    }

    fun setNavigationBarColor() {
        checkModeState { isDarkMode ->
            if (isDarkMode) {
                activity.get()?.run {
                    window?.navigationBarColor = ContextCompat.getColor(this, R.color.black)
                }
            }
        }
    }

    private fun checkModeState(callBack: (Boolean) -> Unit) {
        activity.get()?.let {
            val sharedPref = SharedPreferencesManager.getInstance(it)
            callBack.invoke(sharedPref.isDarkMode())
        }
    }

    /**
     * Android 12 (Api31)
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun setDarkModeForApi31Above(isDarkMode: Boolean, uiModeManager: UiModeManager?) {
        uiModeManager?.setApplicationNightMode(
            if (isDarkMode) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
        )
    }

    /**
     * Android 9 (Api28) ~ Android 11(Api30)
     */
    private fun setDarkModeForApi28ThroughApi30(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }


    /**
     * Android 4 ~ Android 8.1(Api27)
     */
    private fun setDarkModeForApi27Below(isDarkMode: Boolean, uiModeManager: UiModeManager?) {
        uiModeManager?.nightMode =
            if (isDarkMode) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
    }
}