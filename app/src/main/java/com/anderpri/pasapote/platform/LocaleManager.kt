package com.anderpri.pasapote.platform

/**
 * Manages app locale/language switching and persistence.
 * Android: LocaleManager (API 33+) / AppCompatDelegate + SharedPreferences
 * iOS: NSLocale + NSUserDefaults
 */
interface LocaleManager {
    fun changeLanguage(languageCode: String)
    fun getLanguage(): String
    fun setLanguageOnCreate()
}
