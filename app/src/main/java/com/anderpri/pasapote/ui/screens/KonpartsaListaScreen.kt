package com.anderpri.pasapote.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.ui.composables.lista.KonpartsaListaCardAnimated
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel

@Composable
fun KonpartsaListaScreen(
    paddingValues: PaddingValues,
    viewModel: KonpartsaViewModel = hiltViewModel()
) {
    val konpartsak = viewModel.konpartsak.collectAsState().value

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ){
        ListaColumn(paddingValues, konpartsak)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ListaColumn(
    paddingValues: PaddingValues,
    konpartsak: List<Konpartsa>
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier
    ) {
        items(konpartsak) { konpartsa ->
            KonpartsaListaCardAnimated(konpartsa)
        }
    }
}