package com.anderpri.pasapote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.anderpri.pasapote.resources.Res
import com.anderpri.pasapote.resources.gasoekone
import org.jetbrains.compose.resources.Font

@Composable
fun appTypography(): Typography {
    val gasoekOneFF = FontFamily(Font(Res.font.gasoekone))
    val defaultTypography = Typography()
    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = gasoekOneFF),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = gasoekOneFF),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = gasoekOneFF),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = gasoekOneFF),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = gasoekOneFF),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = gasoekOneFF),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = gasoekOneFF),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = gasoekOneFF),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = gasoekOneFF),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = gasoekOneFF),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = gasoekOneFF),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = gasoekOneFF),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = gasoekOneFF),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = gasoekOneFF),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = gasoekOneFF)
    )
}
