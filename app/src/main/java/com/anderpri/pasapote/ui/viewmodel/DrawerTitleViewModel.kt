package com.anderpri.pasapote.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.anderpri.pasapote.ui.state.DrawerTitleState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerTitleViewModel @Inject constructor(
    val drawerTitleState: DrawerTitleState
) : ViewModel() {

    fun updateTitle(newTitleResId: Int) {
        drawerTitleState.updateTitle(newTitleResId)
    }
}