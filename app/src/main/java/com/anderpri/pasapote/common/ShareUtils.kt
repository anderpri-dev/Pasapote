package com.anderpri.pasapote.common

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import java.io.ByteArrayOutputStream

suspend fun GraphicsLayer.toImageBytes(): ByteArray {
    val bitmap = this.toImageBitmap().asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
