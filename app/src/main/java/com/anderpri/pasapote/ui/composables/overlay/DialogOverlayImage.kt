package com.anderpri.pasapote.ui.composables.overlay

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.theme.PasapoteTheme

@Composable
fun DialogOverlayImage(konpartsa: Konpartsa, onPainted: (GraphicsLayer) -> Unit) {
    val graphicsLayer = rememberGraphicsLayer()
    Box(
        modifier = Modifier
            //.fillMaxSize()
            .aspectRatio(9f / 16f)
            .drawWithContent {
                // call record to capture the content in the graphics layer
                graphicsLayer.record {
                    // draw the contents of the composable into the graphics layer
                    this@drawWithContent.drawContent()
                }
                // draw the graphics layer on the visible canvas
                drawLayer(graphicsLayer)
                onPainted(graphicsLayer)
            }
    ) {

        // Argazkia
        AsyncImage(
            model = konpartsa.imagePath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(9f / 16f).fillMaxHeight(),
        )


        // Atzeko irudia
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(9f / 16f).fillMaxHeight(),
            alpha = 0.2f
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OverlayZenbakia(konpartsa)
                Spacer(modifier = Modifier.width(16.dp))
                OverlayKamiseta(konpartsa)
            }
            OverlayIzena(konpartsa)
        }

        OverlayEsteka()
    }
}


@Preview
@Composable
fun DialogOverlayImagePreview() {
    val konpartsa = Konpartsa(
        id = "1",
        number = "1",
        name = "Konpartsa ",
        color = "#FF5733",
        year = "TODO()",
        place = "TODO()",
        txupineras = listOf("TODO()")
    )
    PasapoteTheme {
        DialogOverlayImage(
            konpartsa = konpartsa,
            onPainted = {}
        )
    }
}