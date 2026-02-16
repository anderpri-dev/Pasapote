package com.anderpri.pasapote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.anderpri.pasapote.platform.LocaleManager
import com.anderpri.pasapote.ui.navigation.ApplicationNavigation
import com.anderpri.pasapote.ui.screens.AppDrawer
import com.anderpri.pasapote.ui.theme.PasapoteTheme
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

fun MainViewController() = ComposeUIViewController {
    val localeManager: LocaleManager = koinInject()
    val language by localeManager.currentLanguage.collectAsState()
    var displayLanguage by remember { mutableStateOf(language) }
    var isChangingLanguage by remember { mutableStateOf(false) }
    var restoreRoute by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        localeManager.setLanguageOnCreate()
    }

    LaunchedEffect(language) {
        if (language != displayLanguage) {
            isChangingLanguage = true
            restoreRoute = "settings"
            delay(100)
            displayLanguage = language
            delay(500)
            isChangingLanguage = false
        }
    }

    PasapoteTheme {
        Box(Modifier.fillMaxSize()) {
            key(displayLanguage) {
                val navController = rememberNavController()
                val startRoute = restoreRoute ?: "home"

                AppDrawer(navController) { paddingValues ->
                    ApplicationNavigation(navController, paddingValues, startDestination = startRoute)
                }
            }

            if (isChangingLanguage) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
