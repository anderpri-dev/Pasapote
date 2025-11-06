package com.anderpri.pasapote.di

import com.anderpri.pasapote.data.repository.KonpartsaRepositoryImpl
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import com.anderpri.pasapote.ui.state.DrawerTitleState
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<KonpartsaRepository> {
        KonpartsaRepositoryImpl(get(), get())
    }

    single { DrawerTitleState() }

    viewModel { KonpartsaViewModel(get()) }

    viewModel { DrawerTitleViewModel(get()) }
}