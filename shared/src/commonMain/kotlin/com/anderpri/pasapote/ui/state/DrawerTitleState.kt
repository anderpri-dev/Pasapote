package com.anderpri.pasapote.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DrawerTitleState(defaultTitleResId: Int = 0) {
    private val _title = MutableStateFlow(defaultTitleResId)
    val title: StateFlow<Int> = _title.asStateFlow()

    fun updateTitle(newTitleResId: Int) {
        _title.value = newTitleResId
    }
}
