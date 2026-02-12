@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.anderpri.pasapote.platform.ios

import com.anderpri.pasapote.platform.ShareService
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

class IosShareService : ShareService {

    override fun shareImage(imageBytes: ByteArray, title: String) {
        val nsData = imageBytes.toNSData()
        val image = UIImage(data = nsData) ?: return
        dispatch_async(dispatch_get_main_queue()) {
            val activityVC = UIActivityViewController(
                activityItems = listOf(image),
                applicationActivities = null
            )
            topViewController()?.presentViewController(activityVC, animated = true, completion = null)
        }
    }

    override fun openUrl(url: String) {
        val nsUrl = NSURL(string = url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }

    override fun sendEmail(address: String) {
        val nsUrl = NSURL(string = "mailto:$address") ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}

private fun ByteArray.toNSData(): NSData = usePinned { pinned ->
    NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
}
