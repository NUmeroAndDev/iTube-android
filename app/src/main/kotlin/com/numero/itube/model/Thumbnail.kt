package com.numero.itube.model

import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class Thumbnail(
        val url: String,
        val width: Int,
        val height: Int
) : Serializable