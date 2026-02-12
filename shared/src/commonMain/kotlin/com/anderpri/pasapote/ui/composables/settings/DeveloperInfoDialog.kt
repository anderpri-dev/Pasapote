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
import com.anderpri.pasapote.resources.devinfo_contact
import com.anderpri.pasapote.resources.devinfo_position
import com.anderpri.pasapote.resources.garatzaileari_buruz
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DeveloperInfoDialog(onDismiss: () -> Unit) {
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
                    stringResource(Res.string.garatzaileari_buruz),
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
                        .data(Res.getUri("files/ander.jpg"))
                        .transformations(RoundedCornersTransformation(80f))
                        .build(),
                    contentDescription = null,
                    placeholder = ColorPainter(Color.Gray),
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .clip(CircleShape)
                        .size(120.dp)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
                Text("ANDERPRI", style = MaterialTheme.typography.titleLarge)
                Text(
                    stringResource(Res.string.devinfo_position),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Button(
                        modifier = Modifier.width(200.dp),
                        onClick = { shareService.openUrl("https://www.linkedin.com/in/ander-prieto/") }
                    ) {
                        Text("LinkedIn")
                    }
                    Button(
                        modifier = Modifier.width(200.dp),
                        onClick = { shareService.openUrl("https://github.com/anderpri-dev/") }
                    ) {
                        Text("GitHub")
                    }
                    Button(
                        modifier = Modifier.width(200.dp),
                        onClick = { shareService.sendEmail("anderpri.dev@gmail.com") }
                    ) {
                        Text(stringResource(Res.string.devinfo_contact))
                    }
                }
            }
        }
    )
}
