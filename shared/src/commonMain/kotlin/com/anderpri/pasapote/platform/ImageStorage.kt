package com.anderpri.pasapote.platform

/**
 * Manages local image file storage (save, delete, read).
 * Android: Context.filesDir / cacheDir / ContentResolver
 * iOS: NSFileManager + NSDocumentDirectory
 */
interface ImageStorage {
    suspend fun copyImageToStorage(platformUri: String, id: String): String
    fun deleteImage(imagePath: String): Boolean
    fun deleteAllFiles()
    fun resolveImagePath(storedPath: String): String
}
