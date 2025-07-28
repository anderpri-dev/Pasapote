package com.anderpri.pasapote.domain.repository

import com.anderpri.pasapote.domain.model.Konpartsa
import kotlinx.coroutines.flow.Flow

interface KonpartsaRepository {
    fun getAllKonpartsak(): Flow<List<Konpartsa>>
    fun getAllKonpartsakInYear(year: String): Flow<List<Konpartsa>>
    suspend fun insertAll(konpartsak: List<Konpartsa>)
    suspend fun insertKonpartsaImage(konpartsaId: String, year: String, imageUrl: String)
}