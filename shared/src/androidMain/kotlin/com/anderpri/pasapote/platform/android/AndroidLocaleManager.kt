package com.anderpri.pasapote.platform.android

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class AndroidLocaleManager(private val context: Context) : com.anderpri.pasapote.platform.LocaleManager {
    private val _currentLanguage = MutableStateFlow(getLanguage())
    override val currentLanguage: StateFlow<String> = _currentLanguage

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
        Locale.setDefault(Locale.forLanguageTag(languageCode))
        _currentLanguage.value = languageCode
    }

    override fun getLanguage(): String {
        return getSettings().getString("language", "eu")
    }

    override fun setLanguageOnCreate() {
        val language = getSettings().getString("language", "eu")
        Locale.setDefault(Locale.forLanguageTag(language))
        applyLocale(language)
    }
}
