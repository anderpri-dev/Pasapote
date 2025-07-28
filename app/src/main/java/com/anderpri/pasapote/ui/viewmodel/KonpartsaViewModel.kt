package com.anderpri.pasapote.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.core.content.FileProvider
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

    fun updateKonpartsa(konpartsa: Konpartsa) {
        viewModelScope.launch { repository.updateKonpartsa(konpartsa) }
    }

    fun onImageSelected(konpartsa: Konpartsa, uri: Uri, context: Context) {
        viewModelScope.launch {
            val path = saveImageToInternalStorage(uri, konpartsa.id, context)
            val updatedKonpartsa = konpartsa.copy(imagePath = path)
            repository.updateKonpartsa(updatedKonpartsa)
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

    fun shareImage(imagePath: String, context: Context) {
        val file = File(imagePath)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(shareIntent, "Compartir imagen")
        )
    }

    // share instagram

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