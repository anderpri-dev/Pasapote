package com.anderpri.pasapote.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.anderpri.pasapote.ui.state.DrawerTitleState
import org.jetbrains.compose.resources.StringResource

class DrawerTitleViewModel(
    val drawerTitleState: DrawerTitleState
) : ViewModel() {

    fun updateTitle(newTitle: StringResource) {
        drawerTitleState.updateTitle(newTitle)
    }
}
