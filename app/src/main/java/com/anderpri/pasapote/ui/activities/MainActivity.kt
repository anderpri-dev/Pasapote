package com.anderpri.pasapote.ui.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.anderpri.pasapote.platform.LocaleManager
import com.anderpri.pasapote.ui.navigation.ApplicationNavigation
import com.anderpri.pasapote.ui.screens.AppDrawer
import com.anderpri.pasapote.ui.theme.PasapoteTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val localeManager: LocaleManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @SuppressLint("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        localeManager.setLanguageOnCreate()
        setContent {
            PasapoteTheme {
                val navController = rememberNavController()
                AppDrawer(navController) { paddingValues ->
                    ApplicationNavigation(navController, paddingValues)
                }
            }
        }
    }
}
