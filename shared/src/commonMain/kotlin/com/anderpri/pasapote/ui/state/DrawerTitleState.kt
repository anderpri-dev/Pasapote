package com.anderpri.pasapote.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.StringResource

class DrawerTitleState(defaultTitle: StringResource? = null) {
    private val _title = MutableStateFlow(defaultTitle)
    val title: StateFlow<StringResource?> = _title.asStateFlow()

    fun updateTitle(newTitle: StringResource) {
        _title.value = newTitle
    }
}
