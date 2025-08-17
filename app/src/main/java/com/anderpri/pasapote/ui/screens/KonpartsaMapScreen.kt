package com.anderpri.pasapote.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.composables.card.KonpartsaCard
import com.anderpri.pasapote.ui.theme.AppGreen
import com.anderpri.pasapote.ui.theme.AppRed
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel

@Composable
fun KonpartsaMapScreen(
    paddingValues: PaddingValues,
    drawerTitleViewModel: DrawerTitleViewModel = hiltViewModel(),
    viewModel: KonpartsaViewModel = hiltViewModel()
) {
    val konpartsak = viewModel.konpartsak.collectAsState().value

    LaunchedEffect(Unit) {
        drawerTitleViewModel.updateTitle(R.string.mapa)
    }

    var isLoading by remember { mutableStateOf(true) }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    MapaComposable(paddingValues, konpartsak, onDraw = { isLoading = false })
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun MapaComposable(
    paddingValues: PaddingValues,
    konpartsak: List<Konpartsa>,
    onDraw: () -> Unit
) {
    LaunchedEffect(Unit) { onDraw() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        val boxWidth = constraints.maxWidth.toFloat()
        val boxHeight = constraints.maxHeight.toFloat()

        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = R.drawable.mapa_konpartsak,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        var isCheckedBeteGabe by rememberSaveable { mutableStateOf(false) }
        var isCheckedBeteta by rememberSaveable { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column {
                Button(
                    onClick = { isCheckedBeteGabe = !isCheckedBeteGabe
                        if (isCheckedBeteGabe) isCheckedBeteta = false },
                    modifier = Modifier.width(275.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isCheckedBeteGabe) AppRed else AppGreen,
                        contentColor = Color.White
                    ),
                ) {
                    Text(if (isCheckedBeteGabe) stringResource(R.string.iluntzea_desaktibatu) else stringResource(R.string.bete_gabekoak_ilundu))
                }
                Button(
                    onClick = { isCheckedBeteta = !isCheckedBeteta
                        if (isCheckedBeteta) isCheckedBeteGabe = false },
                    modifier = Modifier.width(275.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isCheckedBeteta) AppRed else AppGreen,
                        contentColor = Color.White
                    ),
                ) {
                    Text(if (isCheckedBeteta) stringResource(R.string.iluntzea_desaktibatu) else stringResource(R.string.betetakoak_ilundu))
                }
            }
        }

        MapaPuntuak(
            konpartsak = konpartsak,
            boxWidth = boxWidth,
            boxHeight = boxHeight,
            paddingValues = paddingValues,
            isCheckedBeteGabe = isCheckedBeteGabe,
            isCheckedBeteta = isCheckedBeteta
        )
    }
}

@Composable
private fun MapaPuntuak(
    konpartsak: List<Konpartsa>,
    boxWidth: Float,
    boxHeight: Float,
    paddingValues: PaddingValues,
    isCheckedBeteGabe: Boolean,
    isCheckedBeteta: Boolean,
) {

    val selectedKonpartsaId = rememberSaveable { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        konpartsak.take(konpartsak.size).forEachIndexed { index, konpartsa ->
            val density = LocalDensity.current
            val offsetX = with(density) { (konpartsa.posX * boxWidth).toDp() }
            val offsetY = with(density) { (konpartsa.posY * boxHeight).toDp() }
            var alphaValue = 1f
            // argazkirik ez -> ilundu
            if (isCheckedBeteGabe) { alphaValue = if (konpartsa.imagePath != null) 1f else 0.2f }
            // argazkia badago -> ilundu
            else if (isCheckedBeteta) {  alphaValue = if (konpartsa.imagePath != null) 0.2f else 1f }
            Box(
                modifier = Modifier
                    .absoluteOffset(x = offsetX - 20.dp, y = offsetY - 20.dp)
                    .alpha(alphaValue)
                    .size(30.dp)
                    .background(Color(konpartsa.color.toColorInt()), shape = RoundedCornerShape(50))
                    .border(1.dp, Color.White, shape = RoundedCornerShape(50))
                    .clickable { selectedKonpartsaId.value = konpartsa.id },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    konpartsa.number,
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }

        selectedKonpartsaId.value?.let { konpartsaId ->
            Dialog(onDismissRequest = { selectedKonpartsaId.value = null }) {
                Box(
                    modifier = Modifier
                        .padding(paddingValues = paddingValues)
                        .aspectRatio(9f / 16f)
                ) {
                    KonpartsaCard(konpartsaId = konpartsaId)
                }
            }
        }
    }
}