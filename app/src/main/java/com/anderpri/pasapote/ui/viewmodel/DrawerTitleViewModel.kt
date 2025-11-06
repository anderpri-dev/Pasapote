package com.anderpri.pasapote.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.anderpri.pasapote.ui.state.DrawerTitleState

class DrawerTitleViewModel(
    val drawerTitleState: DrawerTitleState
) : ViewModel() {

    fun updateTitle(newTitleResId: Int) {
        drawerTitleState.updateTitle(newTitleResId)
    }
}