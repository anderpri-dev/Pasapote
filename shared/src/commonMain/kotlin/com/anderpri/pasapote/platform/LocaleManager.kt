package com.anderpri.pasapote.platform

import kotlinx.coroutines.flow.StateFlow

/**
 * Manages app locale/language switching and persistence.
 * Android: LocaleManager (API 33+) / AppCompatDelegate + SharedPreferences
 * iOS: NSLocale + NSUserDefaults
 */
interface LocaleManager {
    val currentLanguage: StateFlow<String>
    fun changeLanguage(languageCode: String)
    fun getLanguage(): String
    fun setLanguageOnCreate()
}
