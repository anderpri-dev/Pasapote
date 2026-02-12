package com.anderpri.pasapote.platform.ios

import com.anderpri.pasapote.platform.AssetLoader
import com.anderpri.pasapote.platform.CmpAssetLoader

class IosAssetLoader : AssetLoader {
    private val delegate = CmpAssetLoader()

    override suspend fun loadJsonFromAssets(fileName: String): String =
        delegate.loadJsonFromAssets(fileName)
}
