package com.anderpri.pasapote.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderpri.pasapote.common.saveAsShareableFile
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class KonpartsaViewModel @Inject constructor(
    private val repository: KonpartsaRepository
) : ViewModel() {
    val konpartsak: StateFlow<List<Konpartsa>> =
        repository.getAllKonpartsak()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun initKonpartsak(context: Context) {
        val current = konpartsak.value
        if (current.isEmpty()) {
            viewModelScope.launch {
                val json = context.assets.open("konpartsak.json")
                    .bufferedReader().use { it.readText() }
                val konpartsak = Json.decodeFromString<List<Konpartsa>>(json)
                repository.insertAll(konpartsak)
            }
        }
    }

    fun onImageSelected(konpartsa: Konpartsa, uri: Uri, context: Context) {
        viewModelScope.launch {
            val path = saveImageToInternalStorage(uri, konpartsa.id, context)
            repository.insertKonpartsaImage(
                konpartsaId = konpartsa.id,
                year = konpartsa.year,
                imageUrl = path
            )
        }
    }

    private fun saveImageToInternalStorage(uri: Uri, id: String, context: Context): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileName = "imagen_${id}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream: OutputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    fun deleteImage(konpartsa: Konpartsa) {
        viewModelScope.launch {
            val path = konpartsa.imagePath ?: return@launch
            deleteImageFromInternalStorage(path)
            repository.deleteKonpartsaImage(
                konpartsaId = konpartsa.id,
                year = konpartsa.year,
            )
        }
    }

    private fun deleteImageFromInternalStorage(imagePath: String): Boolean {
        val file = File(imagePath)
        return file.exists() && file.delete()
    }

    fun shareToInstagram(
        graphicsLayer: GraphicsLayer,
        context: Context,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            if (graphicsLayer.size.width > 0 && graphicsLayer.size.height > 0) {
                val uri = graphicsLayer.saveAsShareableFile(context)
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "image/png"
                }
                context.startActivity(
                    Intent.createChooser(shareIntent, "Compartir imagen")
                )
            }
        }
    }
}