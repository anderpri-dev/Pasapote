package com.anderpri.pasapote.ui.screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anderpri.pasapote.R
import com.anderpri.pasapote.common.LanguageChangeHelper
import com.anderpri.pasapote.ui.activities.ScreenCoverLanguageChangeActivity
import com.anderpri.pasapote.ui.composables.settings.AppInfoDialog
import com.anderpri.pasapote.ui.composables.settings.DeveloperInfoDialog
import com.anderpri.pasapote.ui.theme.AppRed
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    drawerTitleViewModel: DrawerTitleViewModel = hiltViewModel(),
    viewModel: KonpartsaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        drawerTitleViewModel.updateTitle(R.string.ezarpenak)
    }

    var selectedLanguage by remember { mutableStateOf(LanguageChangeHelper.getLanguage(context)) }
    val onCurrentLanguageChange: (String) -> Unit = { newLanguage ->
        val intent = Intent(context, ScreenCoverLanguageChangeActivity::class.java)
        context.startActivity(intent)
        if (context is Activity) { context.overridePendingTransition(0, 0)}
        Log.d("SettingsScreen", "Selected language: $newLanguage")
        scope.launch {
            delay(1000)
            Log.d("SettingsScreen", "Changing language to: $newLanguage")
            LanguageChangeHelper.changeLanguage(context, newLanguage)
        }
    }

    var showAppInfo by remember { mutableStateOf(false) }
    var showDeveloperInfo by remember { mutableStateOf(false) }
    var showDeleteArgazkiak by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    stringResource(R.string.itxura),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                // Idioma
                DropdownMenuSetting(
                    label = stringResource(R.string.hizkuntza),
                    options = listOf(
                        stringResource(R.string.euskara) to "eu",
                        stringResource(R.string.gaztelera) to "es",
                    ),
                    selected = selectedLanguage,
                    onSelected = {
                        scope.launch {
                            delay(1000)
                            selectedLanguage = it
                        }
                        onCurrentLanguageChange(it)
                    }
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.alert_ezabatu_guztiak_btn),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = { showDeleteArgazkiak = true },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppRed)
            ) {
                Text(
                    stringResource(R.string.alert_ezabatu_guztiak_btn),
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    stringResource(R.string.honi_buruz),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = { showAppInfo = true }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Text(
                        stringResource(R.string.aplikazioari_buruz)
                    )
                }
                Button(onClick = { showDeveloperInfo = true }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Text(
                        stringResource(R.string.garatzaileari_buruz)
                    )
                }
            }
        }
    }

    if (showAppInfo) {
        AppInfoDialog(onDismiss = { showAppInfo = false })
    }
    if (showDeveloperInfo) {
        DeveloperInfoDialog(onDismiss = { showDeveloperInfo = false })
    }
    if (showDeleteArgazkiak) {
        AlertDialog(
            onDismissRequest = { showDeleteArgazkiak = false },
            title = { Text(stringResource(R.string.alert_ezabatu_guztiak_title)) },
            text = { Text(stringResource(R.string.alert_ezabatu_guztiak_subtitle)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteImages(context)
                        showDeleteArgazkiak = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppRed)
                ) {
                    Text(stringResource(R.string.ezabatu))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteArgazkiak = false }) {
                    Text(stringResource(R.string.utzi))
                }
            }
        )
    }
}

@Composable
fun DropdownMenuSetting(
    label: String,
    options: List<Pair<String, String>>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        ) {
            Text("$label: ${options.find { it.second == selected }?.first ?: ""}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (text, value) ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}