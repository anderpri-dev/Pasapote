package com.anderpri.pasapote.platform

import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual suspend fun GraphicsLayer.toImageBytes(): ByteArray {
    val bitmap = toImageBitmap().asSkiaBitmap()
    val image = Image.makeFromBitmap(bitmap)
    val data = image.encodeToData(EncodedImageFormat.PNG) ?: return byteArrayOf()
    return data.bytes
}
