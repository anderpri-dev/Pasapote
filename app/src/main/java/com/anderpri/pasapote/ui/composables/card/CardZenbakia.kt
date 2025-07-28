package com.anderpri.pasapote.ui.composables.card


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.anderpri.pasapote.domain.model.Konpartsa


@Composable
fun CardZenbakia(konpartsa: Konpartsa) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(Color(konpartsa.color.toColorInt()), shape = RoundedCornerShape(50))
            .border(2.dp, Color.White, shape = RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            konpartsa.number,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
    }
}