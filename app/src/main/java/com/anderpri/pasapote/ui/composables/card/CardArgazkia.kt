package com.anderpri.pasapote.ui.composables.card


import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.anderpri.pasapote.domain.model.Konpartsa


@Composable
fun CardArgazkia(
    imagePath: String?,
    konpartsa: Konpartsa,
    onTap: () -> Unit,
    onLongTap: () -> Unit
) {

    Box(
        modifier = Modifier
            //.fillMaxWidth()
            //.aspectRatio(9f / 16f)
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = { onTap() },
                onLongClick = { onLongTap() }
            ),
        contentAlignment = Alignment.Center,
        ) {
        if (imagePath != null) {
            AsyncImage(
                model = imagePath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    //.fillMaxWidth()
                    //.aspectRatio(9f / 16f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
            // Kamisetaren itzala
            AsyncImage(
                model = "file:///android_asset/${konpartsa.id}.png",
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(10.dp)),
                alpha = 0.4f,
                colorFilter = ColorFilter.colorMatrix(
                    ColorMatrix().apply { setToSaturation(0f) }
                )
            )
        }
    }
}