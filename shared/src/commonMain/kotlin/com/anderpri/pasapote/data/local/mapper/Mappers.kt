package com.anderpri.pasapote.data.local.mapper

import com.anderpri.pasapote.data.local.entity.KonpartsaEntity
import com.anderpri.pasapote.data.local.entity.KonpartsaWithImage
import com.anderpri.pasapote.domain.model.Konpartsa

fun KonpartsaEntity.toDomain(imageUrl: String?): Konpartsa =
    Konpartsa(id, number, name, year, place, txupineras.split("|"), color, posX, posY, imageUrl)

fun Konpartsa.toEntity(): KonpartsaEntity =
    KonpartsaEntity(id, number, name, year, place, txupineras.joinToString("|"), color, posX, posY)

fun KonpartsaWithImage.toDomain(): Konpartsa =
    Konpartsa(
        konpartsa.id,
        konpartsa.number,
        konpartsa.name,
        konpartsa.year,
        konpartsa.place,
        if (konpartsa.txupineras.isEmpty()) emptyList() else konpartsa.txupineras.split("|"),
        konpartsa.color,
        konpartsa.posX,
        konpartsa.posY,
        imageUrl
    )