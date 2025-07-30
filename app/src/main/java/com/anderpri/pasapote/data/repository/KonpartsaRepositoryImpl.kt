package com.anderpri.pasapote.data.repository

import com.anderpri.pasapote.data.local.dao.KonpartsaDao
import com.anderpri.pasapote.data.local.dao.KonpartsaImageDao
import com.anderpri.pasapote.data.local.entity.KonpartsaImageEntity
import com.anderpri.pasapote.data.local.mapper.toDomain
import com.anderpri.pasapote.data.local.mapper.toEntity
import com.anderpri.pasapote.domain.model.Konpartsa
import com.anderpri.pasapote.domain.repository.KonpartsaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class KonpartsaRepositoryImpl(
    private val dao: KonpartsaDao,
    private val imageDao: KonpartsaImageDao
) : KonpartsaRepository {

    val year: String = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString()

    override fun getAllKonpartsak(): Flow<List<Konpartsa>> =
        dao.getAllWithImage().map { list ->
            list.map { konpartsaWithImage ->
                konpartsaWithImage.toDomain()
            }
        }

    override fun getAllKonpartsakInYear(year: String): Flow<List<Konpartsa>> {
        // Urte jakin bateko konpartsak lortu
        return emptyFlow()
    }

    override suspend fun insertAll(konpartsak: List<Konpartsa>) {
        konpartsak.forEach { konpartsa ->
            dao.insert(konpartsa.toEntity())
        }
    }

    override suspend fun insertKonpartsaImage(konpartsaId: String, year: String, imageUrl: String) {
        imageDao.insert(
            KonpartsaImageEntity(
                konpartsaId = konpartsaId,
                year = year,
                imageUrl = imageUrl
            )
        )
    }

    override suspend fun deleteKonpartsaImage(konpartsaId: String, year: String) {
        imageDao.deleteByKonpartsaIdAndYear(konpartsaId, year)
    }
}
