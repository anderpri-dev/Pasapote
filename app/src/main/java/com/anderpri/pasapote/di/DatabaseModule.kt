package com.anderpri.pasapote.di

import android.app.Application
import androidx.room.Room
import com.anderpri.pasapote.data.local.AppDatabase
import com.anderpri.pasapote.data.local.MIGRATION_1_2
import com.anderpri.pasapote.data.local.dao.KonpartsaDao
import com.anderpri.pasapote.data.local.dao.KonpartsaImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "app_db")
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun provideKonpartsaDao(db: AppDatabase): KonpartsaDao = db.konpartsaDao()

    @Provides
    fun provideKonpartsaImageDao(db: AppDatabase): KonpartsaImageDao = db.konpartsaImageDao()

}