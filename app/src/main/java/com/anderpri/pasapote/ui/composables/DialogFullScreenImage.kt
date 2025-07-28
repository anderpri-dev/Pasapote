package com.anderpri.pasapote.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.anderpri.pasapote.R

@Composable
fun DialogFullScreenImage(imagePath: String?, onDismiss: () -> Unit, onShare: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier,
            shape = RoundedCornerShape(10.dp),
        ) {
            Column {
                AsyncImage(
                    model = imagePath,
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.background(Color.Black)
                )
            }
            Button(onClick = { onShare() }) {
                Text(stringResource(R.string.partekatu))
            }
        }
    }
}