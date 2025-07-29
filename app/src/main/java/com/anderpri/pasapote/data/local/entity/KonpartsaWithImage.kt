package com.anderpri.pasapote.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class KonpartsaWithImage(
    @Embedded val konpartsa: KonpartsaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "konpartsaId",
        entity = KonpartsaImageEntity::class,
        projection = ["imageUrl"]
    )
    val imageUrl: String?
)