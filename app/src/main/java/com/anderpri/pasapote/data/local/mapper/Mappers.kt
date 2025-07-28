package com.anderpri.pasapote.data.local.mapper

import com.anderpri.pasapote.data.local.entity.KonpartsaEntity
import com.anderpri.pasapote.domain.model.Konpartsa

fun KonpartsaEntity.toDomain(): Konpartsa =
    Konpartsa(id, number, name, year, place, txupineras.split("|"), color, imagePath)

fun Konpartsa.toEntity(): KonpartsaEntity =
    KonpartsaEntity(id, number, name, year, place, txupineras.joinToString("|"), color, imagePath)