package com.anderpri.pasapote.ui.util

import androidx.compose.ui.graphics.Color

fun parseHexColor(hexString: String): Color {
    val hex = hexString.removePrefix("#")
    return Color(("ff$hex").toLong(16))
}
