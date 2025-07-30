package com.anderpri.pasapote.ui.composables.lista

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KonpartsaListaCardAnimated(
    konpartsa: Konpartsa
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { expanded = !expanded }
    ) {
        Column {
            HeaderListaCard(konpartsa)
            AnimatedContent(
                targetState = expanded,
                label = "BodyListaCardAnimation",
                transitionSpec = {
                    if (targetState) {
                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -height } + fadeOut()
                        )
                    } else {
                        (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> height } + fadeOut()
                        )
                    }
                }
            ) { showBody ->
                if (showBody) {
                    BodyListaCard(konpartsa)
                }
            }
        }
    }
}

@Composable
private fun HeaderListaCard(konpartsa: Konpartsa) {
    Box(
        modifier = Modifier
            .background(
                Color(konpartsa.color.toColorInt())
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color(konpartsa.color.toColorInt()),
                            shape = RoundedCornerShape(50)
                        )
                        .border(2.dp, Color.White, shape = RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        konpartsa.number,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                    Spacer(Modifier.width(16.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        konpartsa.name.uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }

            }

        }
    }
}

@Composable
private fun BodyListaCard(konpartsa: Konpartsa) {
    Box(modifier = Modifier.padding(16.dp)) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        stringResource(R.string.sorte_data),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(konpartsa.year, style = MaterialTheme.typography.bodyMedium)

                    Spacer(Modifier.height(4.dp))

                    Text(
                        stringResource(R.string.kokalekua),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(konpartsa.place, style = MaterialTheme.typography.bodyMedium)
                }

                AsyncImage(
                    model = "file:///android_asset/${konpartsa.id}.png",
                    contentDescription = null,
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp),
                    contentScale = ContentScale.Fit,
                )
            }

            if (konpartsa.txupineras.isNotEmpty()) {
                HorizontalDivider()
                Spacer(Modifier.height(4.dp))
                Column {
                    Text(
                        stringResource(R.string.txupinerak),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    konpartsa.txupineras.forEach {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}
