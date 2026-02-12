package com.anderpri.pasapote.platform

/**
 * Loads bundled assets (JSON, etc.) from the platform's asset system.
 * Android: Context.assets | iOS: NSBundle.mainBundle
 */
interface AssetLoader {
    suspend fun loadJsonFromAssets(fileName: String): String
}
