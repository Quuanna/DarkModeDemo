package com.anna.darkmode

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.anna.darkmode.databinding.ActivityMainBinding
import com.anna.darkmode.manager.DarkModeManager
import com.anna.darkmode.manager.SharedPreferencesManager

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val sharedPref by lazy { SharedPreferencesManager.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        setMode()
        setupDarkSwitch()
    }

    private fun setupDarkSwitch() {
        binding?.darkSwitch?.apply {
            isChecked = sharedPref.isDarkMode()

            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    sharedPref.saveDarkMode(true)
                    reStartAPP()
                } else {
                    sharedPref.saveDarkMode(false)
                    reStartAPP()
                }
            }
        }
    }

    /**
     * 初始化時相關設定
     */
    private fun setMode() {
        val darkModeManager = DarkModeManager.getInstance(this)
        darkModeManager.setDarkModeForActivity()
    }

    private fun reStartAPP() {
        Handler().postDelayed({
            val launchIntent = packageManager.getLaunchIntentForPackage(application.packageName)
            launchIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(launchIntent)
            finish()
        }, 100)
    }

}