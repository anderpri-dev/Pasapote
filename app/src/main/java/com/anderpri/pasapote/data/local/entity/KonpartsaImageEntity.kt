package com.anderpri.pasapote.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "konpartsa_image")
data class KonpartsaImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val konpartsaId: String,
    val year: String,
    val imageUrl: String
)