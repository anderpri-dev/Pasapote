package com.anderpri.pasapote.ui.composables.card

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.composables.overlay.DialogFullScreenImageOverlay
import com.anderpri.pasapote.ui.theme.AppRed
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel

@Composable
fun KonpartsaCard(
    konpartsa: Konpartsa,
    viewModel: KonpartsaViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val imagePath = konpartsa.imagePath
    var showFullScreen by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let {
            viewModel.onImageSelected(konpartsa, it, context)
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
        onClick = {  }
    ) {
        Box {
            // Atzeko irudia
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.5f
            )
            // Edukia
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Zenbakia eta izena
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

                    // Argazkia
                    CardArgazkia(
                        imagePath = imagePath,
                        konpartsa = konpartsa,
                        onTap = {
                            if (imagePath == null) launcher.launch("image/jpeg")
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
        DialogFullScreenImageOverlay(
            konpartsa = konpartsa,
            onDismiss = { showFullScreen = false },
            onShareToInstagram = { graphicsLayer ->
                viewModel.shareToInstagram(graphicsLayer, context, coroutineScope)
            },
            onDelete = {
                showDeleteDialog = true
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.alert_ezabatu_title)) },
            text = { Text(stringResource(R.string.alert_ezabatu_subtitle)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteImage(konpartsa)
                        showDeleteDialog = false
                        showFullScreen = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppRed)
                ) {
                    Text(stringResource(R.string.ezabatu))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.utzi))
                }
            }
        )
    }
}