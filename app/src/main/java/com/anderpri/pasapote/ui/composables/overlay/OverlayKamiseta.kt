package com.anderpri.pasapote.ui.composables.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.anderpri.pasapote.domain.model.Konpartsa

@Composable
fun OverlayKamiseta(konpartsa: Konpartsa) {
    Box {
        // Itzala
        AsyncImage(
            model = "file:///android_asset/${konpartsa.id}.png",
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(60.dp)
                .graphicsLayer {
                    alpha = 0.5f
                    scaleX = 1.02f
                    scaleY = 1.02f
                    renderEffect = BlurEffect(16f, 16f)
                }
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black,
                                Color.Transparent
                            ),
                            startY = size.height * 0.85f,
                            endY = size.height * 0.95f
                        ),
                        blendMode = BlendMode.DstIn
                    )
                },
            colorFilter = ColorFilter.tint(Color.Black)
        )
        // Irudi originala
        AsyncImage(
            model = "file:///android_asset/${konpartsa.id}.png",
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(60.dp)
                .clip(RoundedCornerShape(10.dp)),
        )
    }
}