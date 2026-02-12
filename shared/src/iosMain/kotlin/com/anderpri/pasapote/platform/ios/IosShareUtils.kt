package com.anderpri.pasapote.platform

import androidx.compose.ui.graphics.layer.GraphicsLayer

actual suspend fun GraphicsLayer.toImageBytes(): ByteArray {
    // TODO: iOS implementation
    return byteArrayOf()
}
