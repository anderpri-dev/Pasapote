package com.anderpri.pasapote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.anderpri.pasapote.R

val GasoekOneFF = FontFamily(
    Font(R.font.gasoekone)
)

private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = GasoekOneFF),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = GasoekOneFF),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = GasoekOneFF),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = GasoekOneFF),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = GasoekOneFF),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = GasoekOneFF),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = GasoekOneFF),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = GasoekOneFF),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = GasoekOneFF),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = GasoekOneFF),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = GasoekOneFF),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = GasoekOneFF),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = GasoekOneFF),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = GasoekOneFF),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = GasoekOneFF)
)