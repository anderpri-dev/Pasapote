package com.anderpri.pasapote.platform.ios

import com.anderpri.pasapote.platform.AssetLoader
import platform.Foundation.NSBundle

class IosAssetLoader : AssetLoader {
    override suspend fun loadJsonFromAssets(fileName: String): String {
        val path = NSBundle.mainBundle.pathForResource(
            name = fileName.substringBeforeLast("."),
            ofType = fileName.substringAfterLast(".")
        ) ?: error("Asset not found: $fileName")
        return platform.Foundation.NSString.stringWithContentsOfFile(
            path,
            encoding = platform.Foundation.NSUTF8StringEncoding,
            error = null,
        ) ?: error("Failed to read asset: $fileName")
    }
}
