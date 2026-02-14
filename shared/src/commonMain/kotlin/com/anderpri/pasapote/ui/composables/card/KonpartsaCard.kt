package com.anderpri.pasapote.ui.composables.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.anderpri.pasapote.platform.UserFeedback
import com.anderpri.pasapote.platform.rememberCameraPicker
import com.anderpri.pasapote.platform.rememberGalleryPicker
import com.anderpri.pasapote.platform.toImageBytes
import com.anderpri.pasapote.resources.Res
import com.anderpri.pasapote.resources.alert_ezabatu_subtitle
import com.anderpri.pasapote.resources.alert_ezabatu_title
import com.anderpri.pasapote.resources.aukeratu_iturria
import com.anderpri.pasapote.resources.background
import com.anderpri.pasapote.resources.ezabatu
import com.anderpri.pasapote.resources.galeria
import com.anderpri.pasapote.resources.irudia_ezabatu_da
import com.anderpri.pasapote.resources.irudia_partekatu
import com.anderpri.pasapote.resources.kamera
import com.anderpri.pasapote.resources.utzi
import com.anderpri.pasapote.ui.composables.overlay.DialogFullScreenImageOverlay
import com.anderpri.pasapote.ui.theme.AppRed
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun KonpartsaCard(
    konpartsaId: String,
    viewModel: KonpartsaViewModel = koinViewModel(),
) {
    val konpartsak = viewModel.konpartsak.collectAsState()
    val konpartsa = konpartsak.value.find { it.id == konpartsaId }
        ?: return

    val userFeedback: UserFeedback = koinInject()
    val imagePath = konpartsa.imagePath
    var showFullScreen by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val launcher = rememberGalleryPicker { uri ->
        uri?.let { viewModel.onImageSelected(konpartsa, it) }
    }

    var showImageSourceDialog by remember { mutableStateOf(false) }
    var pendingPickerAction by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberCameraPicker { uri ->
        uri?.let { viewModel.onImageSelected(konpartsa, it) }
    }

    LaunchedEffect(showImageSourceDialog, pendingPickerAction) {
        if (!showImageSourceDialog && pendingPickerAction != null) {
            when (pendingPickerAction) {
                "gallery" -> launcher()
                "camera" -> cameraLauncher()
            }
            pendingPickerAction = null
        }
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        onClick = { }
    ) {
        Box {
            Image(
                painter = painterResource(Res.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.5f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        modifier = Modifier
                            .height(164.dp)
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CardZenbakia(konpartsa)
                        Spacer(modifier = Modifier.height(8.dp))
                        CardIzena(konpartsa)
                    }

                    CardArgazkia(
                        imagePath = imagePath,
                        konpartsa = konpartsa,
                        onTap = {
                            if (imagePath == null) showImageSourceDialog = true
                            else showFullScreen = true
                        },
                        onLongTap = {
                            if (imagePath != null) showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showFullScreen && imagePath != null) {
        val coroutineScope = rememberCoroutineScope()
        val shareTitle = stringResource(Res.string.irudia_partekatu)
        DialogFullScreenImageOverlay(
            konpartsa = konpartsa,
            onDismiss = { showFullScreen = false },
            onShareToInstagram = { graphicsLayer ->
                coroutineScope.launch {
                    if (graphicsLayer.size.width > 0 && graphicsLayer.size.height > 0) {
                        val bytes = graphicsLayer.toImageBytes()
                        viewModel.shareImage(bytes, shareTitle)
                    }
                }
            },
            onDelete = {
                showDeleteDialog = true
            }
        )
    }

    val deleteMessage = stringResource(Res.string.irudia_ezabatu_da)
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(Res.string.alert_ezabatu_title)) },
            text = { Text(stringResource(Res.string.alert_ezabatu_subtitle)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteImage(konpartsa)
                        showDeleteDialog = false
                        showFullScreen = false
                        userFeedback.showMessage(deleteMessage)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppRed)
                ) {
                    Text(stringResource(Res.string.ezabatu))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(Res.string.utzi))
                }
            }
        )
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            text = { Text(stringResource(Res.string.aukeratu_iturria)) },
            confirmButton = {
                Button(onClick = {
                    pendingPickerAction = "gallery"
                    showImageSourceDialog = false
                }) { Text(stringResource(Res.string.galeria)) }
            },
            dismissButton = {
                Button(onClick = {
                    pendingPickerAction = "camera"
                    showImageSourceDialog = false
                }) { Text(stringResource(Res.string.kamera)) }
            }
        )
    }
}
