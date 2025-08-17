package com.anderpri.pasapote.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anderpri.pasapote.R
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.composables.card.KonpartsaCard
import com.anderpri.pasapote.ui.theme.AppGreen
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel
import kotlinx.coroutines.launch


@Composable
fun KonpartsaCarousel(
    paddingValues: PaddingValues,
    drawerTitleViewModel: DrawerTitleViewModel = hiltViewModel(),
    viewModel: KonpartsaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        drawerTitleViewModel.updateTitle(R.string.pasapotea)
        viewModel.initKonpartsak(context)
    }
    val konpartsak = viewModel.konpartsak.collectAsState().value

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = -0.5f,
        pageCount = { konpartsak.size },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 30.dp, vertical = 20.dp),
            pageSpacing = 10.dp,
            modifier = Modifier.weight(1f)
        ) { KonpartsaCard(konpartsaId = konpartsak[it].id) }
        KonpartsaSlider(konpartsak, pagerState)
    }
}

@Composable
private fun KonpartsaSlider(
    konpartsak: List<Konpartsa>,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()

    if (konpartsak.isNotEmpty()) {
        Box {
            // Puntutxoak
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .height(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                konpartsak.forEachIndexed { index, konpartsa ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (konpartsa.imagePath != null) AppGreen else Color.LightGray,
                                shape = CircleShape
                            )
                    )
                }
            }

            Slider(
                value = pagerState.currentPage.toFloat(),
                onValueChange = { page ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page.toInt())
                    }
                },
                valueRange = 0f..(konpartsak.size - 1).coerceAtLeast(0).toFloat(),
                steps = (konpartsak.size - 2).coerceAtLeast(0),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .height(24.dp),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                )
            )
        }
    }
}