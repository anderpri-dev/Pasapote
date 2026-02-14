package com.anderpri.pasapote.di

import com.anderpri.pasapote.data.local.AppDatabase
import com.anderpri.pasapote.data.local.getAndroidDatabase
import com.anderpri.pasapote.data.repository.KonpartsaRepositoryImpl
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import com.anderpri.pasapote.platform.AssetLoader
import com.anderpri.pasapote.platform.CmpAssetLoader
import com.anderpri.pasapote.platform.ImageStorage
import com.anderpri.pasapote.platform.LocaleManager
import com.anderpri.pasapote.platform.ShareService
import com.anderpri.pasapote.platform.UserFeedback
import com.anderpri.pasapote.platform.android.AndroidImageStorage
import com.anderpri.pasapote.platform.android.AndroidLocaleManager
import com.anderpri.pasapote.platform.android.AndroidShareService
import com.anderpri.pasapote.platform.android.AndroidUserFeedback
import com.anderpri.pasapote.resources.Res
import com.anderpri.pasapote.resources.app_name
import com.anderpri.pasapote.ui.state.DrawerTitleState
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // Database
    single { getAndroidDatabase(androidApplication()) }
    single { get<AppDatabase>().konpartsaDao() }
    single { get<AppDatabase>().konpartsaImageDao() }

    // Repository
    single<KonpartsaRepository> { KonpartsaRepositoryImpl(get(), get(), get()) }

    // Platform
    single<AssetLoader> { CmpAssetLoader() }
    single<ImageStorage> { AndroidImageStorage(androidContext()) }
    single<ShareService> { AndroidShareService(androidContext()) }
    single<UserFeedback> { AndroidUserFeedback(androidContext()) }
    single<LocaleManager> { AndroidLocaleManager(androidContext()) }

    // State
    single { DrawerTitleState(Res.string.app_name) }

    // ViewModels
    viewModelOf(::KonpartsaViewModel)
    viewModelOf(::DrawerTitleViewModel)
}
