package com.anderpri.pasapote.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val AppBlue: Color = Color(0xff1a65ab)
val CardBackground: Color = Color(0xfffaf5e6)


val DarkColorScheme = darkColorScheme(
    primary = AppBlue,
    primaryContainer = CardBackground,
    onPrimaryContainer = Color.Black,
)

val LightColorScheme = lightColorScheme(
    primary = AppBlue,
    primaryContainer = CardBackground,
    onPrimaryContainer = Color.Black,
)