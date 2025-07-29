package com.anderpri.pasapote.ui.composables.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.theme.AppRed

@Composable
fun DialogFullScreenImageOverlay(
    konpartsa: Konpartsa,
    onDismiss: () -> Unit,
    onShareToInstagram: (GraphicsLayer) -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            color = Color.Transparent,
        ) {
            var graphicsLayer: GraphicsLayer? by remember { mutableStateOf(null) }

            Column {
                Surface(
                    modifier = Modifier.aspectRatio(9f / 16f),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    DialogOverlayImage(konpartsa) { graphicsLayer = it }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = { onShareToInstagram(graphicsLayer!!) }) {
                        Text(stringResource(R.string.partekatu))
                    }
                    Button(
                        onClick = { onDelete() },
                        colors = ButtonDefaults.buttonColors(containerColor = AppRed)
                    ) {
                        Text(stringResource(R.string.ezabatu))
                    }
                }
            }
        }
    }
}