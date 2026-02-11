package com.anderpri.pasapote.platform

/**
 * Platform-specific sharing, URL opening and email sending.
 * Android: Intent.ACTION_SEND / ACTION_VIEW / ACTION_SENDTO
 * iOS: UIActivityViewController / UIApplication.shared.open
 */
interface ShareService {
    fun shareImage(imageBytes: ByteArray, title: String)
    fun openUrl(url: String)
    fun sendEmail(address: String)
}
