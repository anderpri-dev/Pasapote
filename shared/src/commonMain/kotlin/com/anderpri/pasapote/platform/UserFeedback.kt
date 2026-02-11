package com.anderpri.pasapote.platform

/**
 * Shows short feedback messages to the user.
 * Android: Toast.makeText()
 * iOS: Native alert or Compose Snackbar
 */
interface UserFeedback {
    fun showMessage(message: String)
}
