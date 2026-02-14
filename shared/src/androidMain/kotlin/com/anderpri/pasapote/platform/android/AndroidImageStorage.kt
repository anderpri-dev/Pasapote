package com.anderpri.pasapote.platform.android

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.anderpri.pasapote.platform.ImageStorage
import java.io.File
import java.io.FileOutputStream

class AndroidImageStorage(private val context: Context) : ImageStorage {

    override suspend fun copyImageToStorage(platformUri: String, id: String): String {
        val inputStream = context.contentResolver.openInputStream(Uri.parse(platformUri))
        val fileName = "${id}.jpg"
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { output ->
            inputStream?.copyTo(output)
            inputStream?.close()
        }
        return file.absolutePath
    }

    override fun deleteImage(imagePath: String): Boolean {
        val file = File(imagePath)
        return file.exists() && file.delete()
    }

    override fun resolveImagePath(storedPath: String): String = storedPath

    override fun deleteAllFiles() {
        val dirs = listOf(
            context.cacheDir,
            context.filesDir,
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        dirs.forEach { dir ->
            dir?.listFiles()?.forEach { file ->
                if (file.isFile) file.delete()
            }
        }
    }
}
