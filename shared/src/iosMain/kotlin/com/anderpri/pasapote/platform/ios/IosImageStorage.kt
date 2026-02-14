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
        val fileName = "${id}.jpg"
        val destPath = "${documentsDirectory()}/$fileName"
        if (fileManager.fileExistsAtPath(destPath)) {
            fileManager.removeItemAtPath(destPath, error = null)
        }
        fileManager.copyItemAtPath(platformUri, toPath = destPath, error = null)
        return fileName
    }

    override fun deleteImage(imagePath: String): Boolean {
        val fullPath = if (imagePath.startsWith("/")) imagePath
            else "${documentsDirectory()}/$imagePath"
        return fileManager.removeItemAtPath(fullPath, error = null)
    }

    override fun resolveImagePath(storedPath: String): String {
        if (storedPath.startsWith("/")) return storedPath
        return "${documentsDirectory()}/$storedPath"
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
