package com.anderpri.pasapote.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberGalleryPicker(onResult: (String?) -> Unit): () -> Unit

@Composable
expect fun rememberCameraPicker(onResult: (String?) -> Unit): () -> Unit
