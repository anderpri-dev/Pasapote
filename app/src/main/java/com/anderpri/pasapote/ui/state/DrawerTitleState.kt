package com.anderpri.pasapote.ui.state

import androidx.compose.runtime.mutableIntStateOf
import com.anderpri.pasapote.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrawerTitleState @Inject constructor() {
    val title = mutableIntStateOf(R.string.app_name)

    fun updateTitle(newTitleResId: Int) {
        title.intValue = newTitleResId
    }
}