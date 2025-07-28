package com.anderpri.pasapote.data.repository

import android.util.Log
import com.anderpri.pasapote.data.local.dao.KonpartsaDao
import com.anderpri.pasapote.data.local.mapper.toDomain
import com.anderpri.pasapote.data.local.mapper.toEntity
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class KonpartsaRepositoryImpl(
    private val dao: KonpartsaDao
) : KonpartsaRepository {
    override fun getAllKonpartsak(): Flow<List<Konpartsa>> =
        dao.getAll().map { list ->
            Log.d("KonpartsaRepositoryImpl", "getAllKonpartsak: ${list.size} items")
            Log.d("KonpartsaRepositoryImpl", "getAllKonpartsak: ${list.joinToString { it.name }}")
            list.map { it.toDomain() }
        }

    override suspend fun updateKonpartsa(konpartsa: Konpartsa) =
        dao.update(konpartsa.toEntity())
}