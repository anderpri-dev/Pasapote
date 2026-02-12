package com.anderpri.pasapote.platform.ios

import com.anderpri.pasapote.platform.UserFeedback
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.NSEC_PER_SEC
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

class IosUserFeedback : UserFeedback {
    override fun showMessage(message: String) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )
        topViewController()?.presentViewController(alert, animated = true) {
            dispatch_after(
                dispatch_time(DISPATCH_TIME_NOW, (1.5 * NSEC_PER_SEC.toDouble()).toLong()),
                dispatch_get_main_queue()
            ) {
                alert.dismissViewControllerAnimated(true, null)
            }
        }
    }
}

internal fun topViewController(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.keyWindow
    var topController = keyWindow?.rootViewController
    while (topController?.presentedViewController != null) {
        topController = topController.presentedViewController
    }
    return topController
}
