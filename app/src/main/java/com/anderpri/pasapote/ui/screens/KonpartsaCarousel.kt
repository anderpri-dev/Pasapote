package com.anderpri.pasapote.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.composables.card.KonpartsaCard
import com.anderpri.pasapote.ui.theme.PasapoteTheme
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel


@Composable
fun KonpartsaCarousel(
    modifier: Modifier = Modifier,
    viewModel: KonpartsaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initKonpartsak(context)
    }
    val konpartsak = viewModel.konpartsak.collectAsState().value

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = -0.5f,
        pageCount = { konpartsak.size },
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 30.dp, vertical = 10.dp),
            pageSpacing = 10.dp,
            modifier = Modifier.fillMaxSize(),
        ) {
            val konpartsa: Konpartsa = konpartsak[it]
            KonpartsaCard(konpartsa = konpartsa)
        }
    }
}


@Preview
@Composable
fun CarouselCardPreview() {
    PasapoteTheme {
        val konpartsa = Konpartsa(
            id = "1",
            number = "1",
            name = "Konpartsa ",
            color = "#FF5733",
            year = "TODO()",
            place = "TODO()",
            txupineras = listOf("TODO()")
        )
        KonpartsaCard(
            konpartsa
        )
    }

}