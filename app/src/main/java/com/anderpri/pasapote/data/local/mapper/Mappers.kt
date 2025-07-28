package com.anderpri.pasapote.data.local.mapper

import com.anderpri.pasapote.data.local.entity.KonpartsaEntity
import com.anderpri.pasapote.domain.model.Konpartsa

fun KonpartsaEntity.toDomain(imageUrl: String?): Konpartsa =
    Konpartsa(id, number, name, year, place, txupineras.split("|"), color, imageUrl)

fun Konpartsa.toEntity(): KonpartsaEntity =
    KonpartsaEntity(id, number, name, year, place, txupineras.joinToString("|"), color)