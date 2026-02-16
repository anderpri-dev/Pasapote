package com.anderpri.pasapote.ui.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.rememberNavController
import com.anderpri.pasapote.platform.LocaleManager
import com.anderpri.pasapote.ui.navigation.ApplicationNavigation
import com.anderpri.pasapote.ui.screens.AppDrawer
import com.anderpri.pasapote.ui.theme.PasapoteTheme
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val localeManager: LocaleManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @SuppressLint("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        localeManager.setLanguageOnCreate()
        setContent {
            val language by localeManager.currentLanguage.collectAsState()
            var displayLanguage by remember { mutableStateOf(language) }
            var isChangingLanguage by remember { mutableStateOf(false) }
            var restoreRoute by remember { mutableStateOf<String?>(null) }

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

            val localizedConfig = remember(displayLanguage) {
                Configuration(resources.configuration).apply {
                    setLocale(Locale.forLanguageTag(displayLanguage))
                }
            }

            CompositionLocalProvider(LocalConfiguration provides localizedConfig) {
                PasapoteTheme {
                    Box(Modifier.fillMaxSize()) {
                        key(displayLanguage) {
                            val navController = rememberNavController()

                            LaunchedEffect(restoreRoute) {
                                if (restoreRoute != null) {
                                    navController.navigate(restoreRoute!!)
                                }
                            }

                            AppDrawer(navController) { paddingValues ->
                                ApplicationNavigation(navController, paddingValues)
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
        }
    }
}
