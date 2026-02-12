@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.anderpri.pasapote.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUUID
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun rememberGalleryPicker(onResult: (String?) -> Unit): () -> Unit {
    val callback = rememberUpdatedState(onResult)

    val delegate = remember {
        GalleryPickerDelegate { path ->
            callback.value(path)
        }
    }

    return {
        val config = PHPickerConfiguration()
        config.selectionLimit = 1
        config.filter = PHPickerFilter.imagesFilter
        val picker = PHPickerViewController(configuration = config)
        picker.delegate = delegate
        getTopViewController()?.presentViewController(picker, animated = true, completion = null)
    }
}

@Composable
actual fun rememberCameraPicker(onResult: (String?) -> Unit): () -> Unit {
    val callback = rememberUpdatedState(onResult)

    val delegate = remember {
        CameraPickerDelegate { path ->
            callback.value(path)
        }
    }

    return {
        if (UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
            val picker = UIImagePickerController()
            picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            picker.delegate = delegate
            getTopViewController()?.presentViewController(picker, animated = true, completion = null)
        } else {
            onResult(null)
        }
    }
}

private class GalleryPickerDelegate(
    private val onResult: (String?) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, null)

        val result = didFinishPicking.firstOrNull() as? PHPickerResult
        if (result == null) {
            onResult(null)
            return
        }

        val provider = result.itemProvider
        if (provider.hasItemConformingToTypeIdentifier("public.image")) {
            provider.loadFileRepresentationForTypeIdentifier("public.image") { url, _ ->
                if (url != null) {
                    val tempDir = NSTemporaryDirectory()
                    val fileName = "gallery_${NSUUID().UUIDString}.jpg"
                    val destPath = "$tempDir$fileName"
                    NSFileManager.defaultManager.copyItemAtPath(
                        url.path!!,
                        toPath = destPath,
                        error = null
                    )
                    dispatch_async(dispatch_get_main_queue()) {
                        onResult(destPath)
                    }
                } else {
                    dispatch_async(dispatch_get_main_queue()) {
                        onResult(null)
                    }
                }
            }
        } else {
            onResult(null)
        }
    }
}

private class CameraPickerDelegate(
    private val onResult: (String?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        picker.dismissViewControllerAnimated(true, null)

        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage]
                as? platform.UIKit.UIImage
        if (image != null) {
            val data: NSData? = UIImageJPEGRepresentation(image, 0.85)
            if (data != null) {
                val tempDir = NSTemporaryDirectory()
                val fileName = "camera_${NSUUID().UUIDString}.jpg"
                val destPath = "$tempDir$fileName"
                NSFileManager.defaultManager.createFileAtPath(destPath, contents = data, attributes = null)
                onResult(destPath)
            } else {
                onResult(null)
            }
        } else {
            onResult(null)
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, null)
        onResult(null)
    }
}

private fun getTopViewController(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.keyWindow
    var topController = keyWindow?.rootViewController
    while (topController?.presentedViewController != null) {
        topController = topController.presentedViewController
    }
    return topController
}
