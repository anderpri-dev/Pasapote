package com.anderpri.pasapote.ui.composables.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa

@Composable
fun DialogFullScreenImageOverlay(
    konpartsa: Konpartsa,
    onDismiss: () -> Unit,
    onShareToInstagram: (GraphicsLayer) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier.aspectRatio(9f / 16f),
            shape = RoundedCornerShape(10.dp),
        ) {

            var graphicsLayer: GraphicsLayer? by remember { mutableStateOf(null) }

            DialogOverlayImage(konpartsa) { it ->
                graphicsLayer = it
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(onClick = { onShareToInstagram(graphicsLayer!!) }) {
                    Text(stringResource(R.string.partekatu))
                }
            }
        }
    }
}