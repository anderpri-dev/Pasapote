package com.anderpri.pasapote.platform.ios

import com.anderpri.pasapote.platform.LocaleManager
import platform.Foundation.NSUserDefaults

class IosLocaleManager : LocaleManager {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun changeLanguage(languageCode: String) {
        defaults.setObject(languageCode, forKey = "language")
        defaults.setObject(listOf(languageCode), forKey = "AppleLanguages")
        defaults.synchronize()
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
