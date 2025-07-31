package com.anderpri.pasapote.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val AppBlue: Color = Color(0xff1a65ab)
val AppGreen: Color = Color(0xff1aab65)
val AppRed: Color = Color(0xffab1a1a)
val CardBackground: Color = Color(0xfffaf5e6)

val DarkColorScheme = darkColorScheme(
    primary = AppBlue,
    onPrimary = Color.White,
    primaryContainer = CardBackground,
    onPrimaryContainer = Color.Black,
)

val LightColorScheme = lightColorScheme(
    primary = AppBlue,
    onPrimary = Color.White,
    primaryContainer = CardBackground,
    onPrimaryContainer = Color.Black,
)