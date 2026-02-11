package com.anderpri.pasapote.platform.ios

import com.anderpri.pasapote.platform.ImageStorage

class IosImageStorage : ImageStorage {
    override suspend fun copyImageToStorage(platformUri: String, id: String): String {
        TODO("iOS image storage not implemented yet")
    }

    override fun deleteImage(imagePath: String): Boolean {
        TODO("iOS image deletion not implemented yet")
    }

    override fun deleteAllFiles() {
        TODO("iOS delete all files not implemented yet")
    }
}
