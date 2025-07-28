package com.anderpri.pasapote.di

import com.anderpri.pasapote.data.local.dao.KonpartsaDao
import com.anderpri.pasapote.data.repository.KonpartsaRepositoryImpl
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideKonpartsaRepository(dao: KonpartsaDao): KonpartsaRepository =
        KonpartsaRepositoryImpl(dao)
}