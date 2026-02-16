package com.anderpri.pasapote.platform.ios

import com.anderpri.pasapote.platform.LocaleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Foundation.NSUserDefaults

class IosLocaleManager : LocaleManager {
    private val defaults = NSUserDefaults.standardUserDefaults
    private val _currentLanguage = MutableStateFlow(getLanguage())
    override val currentLanguage: StateFlow<String> = _currentLanguage

    override fun changeLanguage(languageCode: String) {
        defaults.setObject(languageCode, forKey = "language")
        defaults.setObject(listOf(languageCode), forKey = "AppleLanguages")
        defaults.synchronize()
        _currentLanguage.value = languageCode
    }

    override fun getLanguage(): String {
        return defaults.stringForKey("language") ?: "eu"
    }

    override fun setLanguageOnCreate() {
        val language = getLanguage()
        defaults.setObject(listOf(language), forKey = "AppleLanguages")
        defaults.synchronize()
    }
}
