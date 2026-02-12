package com.anderpri.pasapote

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.anderpri.pasapote.platform.LocaleManager
import com.anderpri.pasapote.ui.navigation.ApplicationNavigation
import com.anderpri.pasapote.ui.screens.AppDrawer
import com.anderpri.pasapote.ui.theme.PasapoteTheme
import org.koin.compose.koinInject

fun MainViewController() = ComposeUIViewController {
    val localeManager: LocaleManager = koinInject()

    LaunchedEffect(Unit) {
        localeManager.setLanguageOnCreate()
    }

    PasapoteTheme {
        val navController = rememberNavController()
        AppDrawer(navController) { paddingValues ->
            ApplicationNavigation(navController, paddingValues)
        }
    }
}
