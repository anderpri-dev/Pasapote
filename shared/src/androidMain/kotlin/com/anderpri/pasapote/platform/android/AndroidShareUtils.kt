package com.anderpri.pasapote.platform

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import java.io.ByteArrayOutputStream

actual suspend fun GraphicsLayer.toImageBytes(): ByteArray {
    val bitmap = this.toImageBitmap().asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
