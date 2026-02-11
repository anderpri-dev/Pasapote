package com.anderpri.pasapote.common

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

object LanguageChangeHelper {

    private fun getSettings(context: Context): Settings =
        SharedPreferencesSettings(
            context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        )

    fun changeLanguage(context: Context, languageCode: String) {
        saveLanguage(context, languageCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }

    fun saveLanguage(context: Context, language: String) {
        getSettings(context).putString("language", language)
    }

    fun getLanguage(context: Context): String {
        return getSettings(context).getString("language", "eu")
    }

    fun setLanguageOnCreate(context: Context) {
        val language = getSettings(context).getString("language", "eu")
        changeLanguage(context, language)
    }
}
