package com.anderpri.pasapote.di

import com.anderpri.pasapote.data.local.AppDatabase
import com.anderpri.pasapote.data.local.getIosDatabase
import com.anderpri.pasapote.data.repository.KonpartsaRepositoryImpl
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import com.anderpri.pasapote.platform.AssetLoader
import com.anderpri.pasapote.platform.CmpAssetLoader
import com.anderpri.pasapote.platform.ImageStorage
import com.anderpri.pasapote.platform.LocaleManager
import com.anderpri.pasapote.platform.ShareService
import com.anderpri.pasapote.platform.UserFeedback
import com.anderpri.pasapote.platform.ios.IosImageStorage
import com.anderpri.pasapote.platform.ios.IosLocaleManager
import com.anderpri.pasapote.platform.ios.IosShareService
import com.anderpri.pasapote.platform.ios.IosUserFeedback
import com.anderpri.pasapote.resources.Res
import com.anderpri.pasapote.resources.app_name
import com.anderpri.pasapote.ui.state.DrawerTitleState
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val iosModule = module {
    // Database
    single { getIosDatabase() }
    single { get<AppDatabase>().konpartsaDao() }
    single { get<AppDatabase>().konpartsaImageDao() }

    // Repository
    single<KonpartsaRepository> { KonpartsaRepositoryImpl(get(), get(), get()) }

    // Platform
    single<AssetLoader> { CmpAssetLoader() }
    single<ImageStorage> { IosImageStorage() }
    single<ShareService> { IosShareService() }
    single<UserFeedback> { IosUserFeedback() }
    single<LocaleManager> { IosLocaleManager() }

    // State
    single { DrawerTitleState(Res.string.app_name) }

    // ViewModels
    viewModelOf(::KonpartsaViewModel)
    viewModelOf(::DrawerTitleViewModel)
}

fun initKoin() {
    startKoin {
        modules(iosModule)
    }
}
