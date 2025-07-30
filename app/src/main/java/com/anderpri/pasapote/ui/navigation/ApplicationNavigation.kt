package com.anderpri.pasapote.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anderpri.pasapote.ui.screens.KonpartsaListaScreen
import com.anderpri.pasapote.ui.screens.KonpartsaMapScreen
import com.anderpri.pasapote.ui.screens.KonpartsaCarousel

@Composable
fun ApplicationNavigation(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = { defaultEnterTransition() },
        exitTransition = { defaultExitTransition() },
        popEnterTransition = { defaultEnterTransition() },
        popExitTransition = { defaultExitTransition() }
    ) {
        composable("home") { KonpartsaCarousel(paddingValues) }
        composable("map") { KonpartsaMapScreen(paddingValues) }
        composable("list") { KonpartsaListaScreen(paddingValues) }
    }
}

fun defaultEnterTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(durationMillis = 300))
}

fun defaultExitTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(durationMillis = 300))
}