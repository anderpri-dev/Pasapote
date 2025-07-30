package com.anderpri.pasapote.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anderpri.pasapote.ui.composables.KonpartsaMapScreen
import com.anderpri.pasapote.ui.screens.KonpartsaCarousel

@Composable
fun ApplicationNavigation(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { KonpartsaCarousel(paddingValues) }
        composable("map") { KonpartsaMapScreen(paddingValues) }
    }
}