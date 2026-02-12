package com.anderpri.pasapote.ui.composables.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.anderpri.pasapote.platform.ShareService
import com.anderpri.pasapote.resources.Res
import com.anderpri.pasapote.resources.app_close
import com.anderpri.pasapote.resources.app_name
import com.anderpri.pasapote.resources.aplikazioari_buruz
import com.anderpri.pasapote.resources.pasapote_info_1
import com.anderpri.pasapote.resources.pasapote_info_2
import com.anderpri.pasapote.resources.sinatu_ekimena
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppInfoDialog(onDismiss: () -> Unit) {
    val shareService: ShareService = koinInject()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(Res.string.app_close))
            }
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    stringResource(Res.string.aplikazioari_buruz),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(Res.getUri("files/ic_launcher-playstore.png"))
                        .transformations(RoundedCornersTransformation(80f))
                        .build(),
                    contentDescription = null,
                    placeholder = ColorPainter(Color.Gray),
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .clip(CircleShape)
                        .size(120.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = CircleShape
                        )
                )
                Text(
                    stringResource(Res.string.app_name).uppercase(),
                    style = MaterialTheme.typography.titleLarge
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        stringResource(Res.string.pasapote_info_1),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                    Button(
                        modifier = Modifier.width(220.dp),
                        onClick = { shareService.openUrl("https://app.bilbokokonpartsak.eus") }
                    ) {
                        Text("APP Aste Nagusia")
                    }
                    Text(
                        stringResource(Res.string.pasapote_info_2),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                    Button(
                        modifier = Modifier.width(220.dp),
                        onClick = { shareService.openUrl("https://www.txosnak.eus") }
                    ) {
                        Text(stringResource(Res.string.sinatu_ekimena))
                    }
                }
            }
        }
    )
}
