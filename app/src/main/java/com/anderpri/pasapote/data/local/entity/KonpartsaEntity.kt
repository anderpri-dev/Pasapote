package com.anderpri.pasapote.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "konpartsa")
data class KonpartsaEntity(
    @PrimaryKey val id: String,
    val number: String,
    val name: String,
    val year: String,
    val place: String,
    val txupineras: String,
    val color: String,
)