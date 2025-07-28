package com.anderpri.pasapote.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.core.content.FileProvider
import java.io.File

// https://towardsdev.com/something-worth-sharing-cf3e3f5083cf
suspend fun GraphicsLayer.saveAsShareableFile(context: Context): Uri? {
    val bitmap = this.toImageBitmap().asAndroidBitmap()
    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "pasapote-${System.currentTimeMillis()}.png"
    )
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
    }
    return file.getShareableUri(context)
}

fun File.getShareableUri(context: Context): Uri {
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        this
    )
}