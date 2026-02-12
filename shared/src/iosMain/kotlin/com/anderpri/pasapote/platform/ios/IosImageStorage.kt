@file:OptIn(ExperimentalForeignApi::class)

package com.anderpri.pasapote.platform.ios

import com.anderpri.pasapote.platform.ImageStorage
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

class IosImageStorage : ImageStorage {
    private val fileManager = NSFileManager.defaultManager

    private fun documentsDirectory(): String {
        val url = fileManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return url!!.path!!
    }

    override suspend fun copyImageToStorage(platformUri: String, id: String): String {
        val destPath = "${documentsDirectory()}/${id}.jpg"
        if (fileManager.fileExistsAtPath(destPath)) {
            fileManager.removeItemAtPath(destPath, error = null)
        }
        fileManager.copyItemAtPath(platformUri, toPath = destPath, error = null)
        return destPath
    }

    override fun deleteImage(imagePath: String): Boolean {
        return fileManager.removeItemAtPath(imagePath, error = null)
    }

    override fun deleteAllFiles() {
        val docsDir = documentsDirectory()
        val contents = fileManager.contentsOfDirectoryAtPath(docsDir, error = null) ?: return
        for (item in contents) {
            val fileName = item as? String ?: continue
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                fileManager.removeItemAtPath("$docsDir/$fileName", error = null)
            }
        }
    }
}
