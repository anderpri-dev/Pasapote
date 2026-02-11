package com.anderpri.pasapote.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.anderpri.pasapote.data.local.AppDatabase
import com.anderpri.pasapote.data.local.MIGRATION_1_2
import com.anderpri.pasapote.data.repository.KonpartsaRepositoryImpl
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import com.anderpri.pasapote.platform.AssetLoader
import com.anderpri.pasapote.platform.ImageStorage
import com.anderpri.pasapote.platform.ShareService
import com.anderpri.pasapote.platform.android.AndroidAssetLoader
import com.anderpri.pasapote.platform.android.AndroidImageStorage
import com.anderpri.pasapote.platform.android.AndroidShareService
import com.anderpri.pasapote.ui.state.DrawerTitleState
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import com.anderpri.pasapote.ui.viewmodel.KonpartsaViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // Database
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "app_db")
            .setDriver(BundledSQLiteDriver())
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    single { get<AppDatabase>().konpartsaDao() }
    single { get<AppDatabase>().konpartsaImageDao() }

    // Repository
    single<KonpartsaRepository> { KonpartsaRepositoryImpl(get(), get()) }

    // Platform
    single<AssetLoader> { AndroidAssetLoader(androidContext()) }
    single<ImageStorage> { AndroidImageStorage(androidContext()) }
    single<ShareService> { AndroidShareService(androidContext()) }

    // State
    singleOf(::DrawerTitleState)

    // ViewModels
    viewModelOf(::KonpartsaViewModel)
    viewModelOf(::DrawerTitleViewModel)
}
