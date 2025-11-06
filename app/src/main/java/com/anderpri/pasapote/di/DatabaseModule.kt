package com.anderpri.pasapote.di

import android.app.Application
import androidx.room.Room
import com.anderpri.pasapote.data.local.AppDatabase
import com.anderpri.pasapote.data.local.MIGRATION_1_2
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get<Application>(), AppDatabase::class.java, "app_db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single { get<AppDatabase>().konpartsaDao() }

    single { get<AppDatabase>().konpartsaImageDao() }
}