package com.anderpri.pasapote.data.local.mapper

import com.anderpri.pasapote.data.local.entity.KonpartsaEntity
import com.anderpri.pasapote.data.local.entity.KonpartsaWithImage
import com.anderpri.pasapote.domain.model.Konpartsa

fun KonpartsaEntity.toDomain(imageUrl: String?): Konpartsa =
    Konpartsa(id, number, name, year, place, txupineras.split("|"), color, imageUrl)

fun Konpartsa.toEntity(): KonpartsaEntity =
    KonpartsaEntity(id, number, name, year, place, txupineras.joinToString("|"), color)

fun KonpartsaWithImage.toDomain(): Konpartsa =
    Konpartsa(
        konpartsa.id,
        konpartsa.number,
        konpartsa.name,
        konpartsa.year,
        konpartsa.place,
        konpartsa.txupineras.split("|"),
        konpartsa.color,
        imageUrl
    )