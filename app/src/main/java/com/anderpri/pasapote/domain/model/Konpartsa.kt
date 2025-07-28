package com.anderpri.pasapote.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Konpartsa(
    val id: String,
    val number: String,
    val name: String,
    val year: String,
    val place: String,
    val txupineras: List<String>,
    val color: String,
    val imagePath: String? = null,
)