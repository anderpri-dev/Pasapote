package com.anderpri.pasapote.ui.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.anderpri.pasapote.ui.navigation.ApplicationNavigation
import com.anderpri.pasapote.ui.screens.AppDrawer
import com.anderpri.pasapote.ui.theme.PasapoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @SuppressLint("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
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