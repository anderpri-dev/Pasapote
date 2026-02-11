package com.anderpri.pasapote.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import com.anderpri.pasapote.platform.AssetLoader
import com.anderpri.pasapote.platform.ImageStorage
import com.anderpri.pasapote.platform.ShareService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class KonpartsaViewModel(
    private val repository: KonpartsaRepository,
    private val assetLoader: AssetLoader,
    private val imageStorage: ImageStorage,
    private val shareService: ShareService
) : ViewModel() {
    val konpartsak: StateFlow<List<Konpartsa>> =
        repository.getAllKonpartsak()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun initKonpartsak() {
        val current = konpartsak.value
        if (current.isEmpty()) {
            viewModelScope.launch {
                val json = assetLoader.loadJsonFromAssets("konpartsak.json")
                val konpartsak = Json.decodeFromString<List<Konpartsa>>(json)
                repository.insertAll(konpartsak)
            }
        }
    }

    fun onImageSelected(konpartsa: Konpartsa, platformUri: String) {
        viewModelScope.launch {
            val path = imageStorage.copyImageToStorage(platformUri, konpartsa.id)
            repository.insertKonpartsaImage(
                konpartsaId = konpartsa.id,
                year = konpartsa.year,
                imageUrl = path
            )
        }
    }

    fun deleteImage(konpartsa: Konpartsa) {
        viewModelScope.launch {
            val path = konpartsa.imagePath ?: return@launch
            imageStorage.deleteImage(path)
            repository.deleteKonpartsaImage(
                konpartsaId = konpartsa.id,
                year = konpartsa.year,
            )
        }
    }

    fun shareImage(imageBytes: ByteArray, title: String) {
        shareService.shareImage(imageBytes, title)
    }

    fun deleteImages() {
        viewModelScope.launch {
            imageStorage.deleteAllFiles()
            repository.deleteAllImages()
        }
    }
}
