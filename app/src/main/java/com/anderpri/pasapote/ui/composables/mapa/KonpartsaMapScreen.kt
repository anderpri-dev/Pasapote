package com.anderpri.pasapote.ui.composables.mapa

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.composables.card.KonpartsaCard
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel

@Composable
fun KonpartsaMapScreen(
    paddingValues: PaddingValues,
    viewModel: KonpartsaViewModel = hiltViewModel()
) {
    val konpartsak = viewModel.konpartsak.collectAsState().value

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


        MapaPuntuak(
            konpartsak,
            boxWidth,
            boxHeight,
            paddingValues,
            onDraw = { onDraw() }
        )

    }
}

@Composable
private fun MapaPuntuak(
    konpartsak: List<Konpartsa>,
    boxWidth: Float,
    boxHeight: Float,
    paddingValues: PaddingValues,
    onDraw: () -> Unit
) {


    val selectedKonpartsaId = rememberSaveable { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        konpartsak.take(konpartsak.size).forEachIndexed { index, konpartsa ->
            //val (posX, posY) = positions[index]
            //val position = positionsList.firstOrNull { it.id == konpartsa.id }

            val posX = konpartsa.posX
            val posY = konpartsa.posY

            val density = LocalDensity.current
            val offsetX = with(density) { (posX * boxWidth).toDp() }
            val offsetY = with(density) { (posY * boxHeight).toDp() }
            Log.d("KonpartsaMapScreen", "Offset in dp: ($offsetX, $offsetY)")


            Box(
                modifier = Modifier
                    .absoluteOffset(x = offsetX - 20.dp, y = offsetY - 20.dp)
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
                        .aspectRatio(9f/16f)
                ){
                    KonpartsaCard(konpartsaId = konpartsaId)
                }
            }
        }

    }
}