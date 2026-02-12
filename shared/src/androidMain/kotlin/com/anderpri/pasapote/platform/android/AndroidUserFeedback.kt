package com.anderpri.pasapote.platform.android

import android.content.Context
import android.widget.Toast
import com.anderpri.pasapote.platform.UserFeedback

class AndroidUserFeedback(private val context: Context) : UserFeedback {
    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
