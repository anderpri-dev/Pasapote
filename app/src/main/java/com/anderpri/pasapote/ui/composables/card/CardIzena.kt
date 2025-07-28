package com.anderpri.pasapote.ui.composables.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.anderpri.pasapote.domain.model.Konpartsa

@Composable
fun CardIzena(konpartsa: Konpartsa) {
    Text(
        konpartsa.name.uppercase(),
        style = MaterialTheme.typography.headlineMedium,
        maxLines = 2,
        textAlign = TextAlign.Center
    )
}