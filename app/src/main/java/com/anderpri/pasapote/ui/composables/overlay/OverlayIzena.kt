package com.anderpri.pasapote.ui.composables.overlay

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextAlign
import com.anderpri.pasapote.domain.model.Konpartsa

@Composable
fun OverlayIzena(konpartsa: Konpartsa) {
    Text(
        konpartsa.name.uppercase(),
        style = MaterialTheme.typography.headlineMedium.copy(
            color = Color.White,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(4f, 4f),
                blurRadius = 10f
            )
        ),
        maxLines = 2,
        textAlign = TextAlign.Center,
    )
}