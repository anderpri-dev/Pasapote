package com.anderpri.pasapote.platform

import com.anderpri.pasapote.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

class CmpAssetLoader : AssetLoader {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun loadJsonFromAssets(fileName: String): String {
        return Res.readBytes("files/$fileName").decodeToString()
    }
}
