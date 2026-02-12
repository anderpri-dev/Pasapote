package com.anderpri.pasapote.platform

import androidx.compose.runtime.Composable

@Composable
actual fun rememberGalleryPicker(onResult: (String?) -> Unit): () -> Unit {
    return { /* TODO: iOS implementation */ }
}

@Composable
actual fun rememberCameraPicker(onResult: (String?) -> Unit): () -> Unit {
    return { /* TODO: iOS implementation */ }
}
