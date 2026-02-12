package com.anderpri.pasapote.platform.android

import android.content.Context
import com.anderpri.pasapote.platform.AssetLoader

class AndroidAssetLoader(private val context: Context) : AssetLoader {
    override suspend fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
