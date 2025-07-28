package com.anderpri.pasapote.domain.repository

import com.anderpri.pasapote.domain.model.Konpartsa
import kotlinx.coroutines.flow.Flow

interface KonpartsaRepository {
    fun getAllKonpartsak(): Flow<List<Konpartsa>>
    suspend fun updateKonpartsa(konpartsa: Konpartsa)
}