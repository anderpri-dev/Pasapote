package com.anderpri.pasapote.ui.state

import androidx.compose.runtime.mutableIntStateOf
import com.anderpri.pasapote.R

class DrawerTitleState {
    val title = mutableIntStateOf(R.string.app_name)

    fun updateTitle(newTitleResId: Int) {
        title.intValue = newTitleResId
    }
}
