package com.anderpri.pasapote.ui.composables

import android.R.attr.contentDescription
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State.Empty.painter
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.composables.card.KonpartsaCard
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel

data class Position(
    val id: String,
    val posX: Float,
    val posY: Float
)

val positionsList: List<Position> =
    listOf(
        Position("aixeberri", 0.72f, 0.80f),
        Position("algara", 0.21f, 0.64f),
        Position("altxaporrue", 0.51f, 0.65f),
        Position("askapena", 0.28f, 0.77f),
        Position("bizizaleak", 0.85f, 0.55f),
        Position("eguzkizaleak", 0.69f, 0.61f),
        Position("hau_pittu_hau", 0.62f, 0.60f),
        Position("hontzak", 0.84f, 0.43f),
        Position("hor_dago_abante", 0.87f, 0.36f),
        Position("kaixo", 0.25f, 0.85f),
        Position("kaskagorri", 0.13f, 0.81f),
        Position("kranba", 0.83f, 0.79f),
        Position("mekaguen", 0.83f, 0.62f),
        Position("moskotarrak", 0.46f, 0.69f),
        Position("pa_ya", 0.20f, 0.79f),
        Position("pinpilipauxa", 0.81f, 0.47f),
        Position("piztiak", 0.79f, 0.83f),
        Position("satorrak", 0.74f, 0.54f),
        Position("sinkuartel", 0.80f, 0.58f),
        Position("tintigorri", 0.81f, 0.92f),
        Position("trikimailu", 0.78f, 0.51f),
        Position("txinbotarrak", 0.57f, 0.63f),
        Position("txinparta_feminista", 0.36f, 0.74f),
        Position("txomin_barullo", 0.89f, 0.30f),
        Position("txori_barrote", 0.68f, 0.57f),
        Position("uribarri", 0.76f, 0.88f),
        Position("zaratas", 0.78f, 0.68f),
    )


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
        konpartsak.take(positionsList.size).forEachIndexed { index, konpartsa ->
            //val (posX, posY) = positions[index]
            val position = positionsList.firstOrNull { it.id == konpartsa.id }

            if (position == null) {
                Log.w("KonpartsaMapScreen", "Position not found for konpartsa: ${konpartsa.id}")
                return@forEachIndexed
            }

            val posX = position.posX
            val posY = position.posY

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