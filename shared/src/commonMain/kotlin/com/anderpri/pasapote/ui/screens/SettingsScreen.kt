package com.anderpri.pasapote.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anderpri.pasapote.platform.LocaleManager
import com.anderpri.pasapote.platform.UserFeedback
import com.anderpri.pasapote.resources.Res
import com.anderpri.pasapote.resources.alert_ezabatu_guztiak_btn
import com.anderpri.pasapote.resources.alert_ezabatu_guztiak_subtitle
import com.anderpri.pasapote.resources.alert_ezabatu_guztiak_title
import com.anderpri.pasapote.resources.aplikazioari_buruz
import com.anderpri.pasapote.resources.euskara
import com.anderpri.pasapote.resources.ezabatu
import com.anderpri.pasapote.resources.ezarpenak
import com.anderpri.pasapote.resources.garatzaileari_buruz
import com.anderpri.pasapote.resources.gaztelera
import com.anderpri.pasapote.resources.hizkuntza
import com.anderpri.pasapote.resources.honi_buruz
import com.anderpri.pasapote.resources.irudiak_ezabatu_dira
import com.anderpri.pasapote.resources.itxura
import com.anderpri.pasapote.resources.utzi
import com.anderpri.pasapote.ui.composables.settings.AppInfoDialog
import com.anderpri.pasapote.ui.composables.settings.DeveloperInfoDialog
import com.anderpri.pasapote.ui.theme.AppRed
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    drawerTitleViewModel: DrawerTitleViewModel = koinViewModel(),
    viewModel: KonpartsaViewModel = koinViewModel()
) {
    val localeManager: LocaleManager = koinInject()
    val userFeedback: UserFeedback = koinInject()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        drawerTitleViewModel.updateTitle(Res.string.ezarpenak)
    }

    var selectedLanguage by remember { mutableStateOf(localeManager.getLanguage()) }

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
                    stringResource(Res.string.itxura),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                DropdownMenuSetting(
                    label = stringResource(Res.string.hizkuntza),
                    options = listOf(
                        stringResource(Res.string.euskara) to "eu",
                        stringResource(Res.string.gaztelera) to "es",
                    ),
                    selected = selectedLanguage,
                    onSelected = {
                        localeManager.changeLanguage(it)
                        scope.launch {
                            delay(1000)
                            selectedLanguage = it
                        }
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
                stringResource(Res.string.alert_ezabatu_guztiak_btn),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = { showDeleteArgazkiak = true },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppRed)
            ) {
                Text(stringResource(Res.string.alert_ezabatu_guztiak_btn))
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    stringResource(Res.string.honi_buruz),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = { showAppInfo = true },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(stringResource(Res.string.aplikazioari_buruz))
                }
                Button(
                    onClick = { showDeveloperInfo = true },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(stringResource(Res.string.garatzaileari_buruz))
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

    val deleteMessage = stringResource(Res.string.irudiak_ezabatu_dira)
    if (showDeleteArgazkiak) {
        AlertDialog(
            onDismissRequest = { showDeleteArgazkiak = false },
            title = { Text(stringResource(Res.string.alert_ezabatu_guztiak_title)) },
            text = { Text(stringResource(Res.string.alert_ezabatu_guztiak_subtitle)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteImages()
                        showDeleteArgazkiak = false
                        userFeedback.showMessage(deleteMessage)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppRed)
                ) {
                    Text(stringResource(Res.string.ezabatu))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteArgazkiak = false }) {
                    Text(stringResource(Res.string.utzi))
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
        contentAlignment = Alignment.Center
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
