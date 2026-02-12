package com.anderpri.pasapote.platform.android

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.LocaleList
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

class AndroidLocaleManager(private val context: Context) : com.anderpri.pasapote.platform.LocaleManager {

    private fun getSettings(): Settings =
        SharedPreferencesSettings(
            context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        )

    private fun applyLocale(languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(android.app.LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }

    override fun changeLanguage(languageCode: String) {
        getSettings().putString("language", languageCode)

        // Launch cover activity to hide locale switch animation
        try {
            val intent = Intent()
            intent.setClassName(context, "com.anderpri.pasapote.ui.activities.ScreenCoverLanguageChangeActivity")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (_: Exception) { }

        // Apply locale after delay to let cover appear
        Handler(Looper.getMainLooper()).postDelayed({
            applyLocale(languageCode)
        }, 1000)
    }

    override fun getLanguage(): String {
        return getSettings().getString("language", "eu")
    }

    override fun setLanguageOnCreate() {
        val language = getSettings().getString("language", "eu")
        applyLocale(language)
    }
}
