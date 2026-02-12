package com.anderpri.pasapote.platform

import androidx.compose.ui.graphics.layer.GraphicsLayer

expect suspend fun GraphicsLayer.toImageBytes(): ByteArray
