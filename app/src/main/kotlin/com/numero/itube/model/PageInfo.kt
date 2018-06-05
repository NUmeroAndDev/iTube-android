package com.numero.itube.model

import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class PageInfo(
        val totalResults: Int,
        val resultsPerPage: Int
) : Serializable