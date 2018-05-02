package com.numero.itube.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class Channel(
        val kind: String,
        val etag: String,
        val id: String,
        val snippet: Snippet
) : Serializable {

    @JsonSerializable
    data class Snippet(
            val title: String,
            val description: String,
            val publishedAt: String,
            val thumbnails: Thumbnails
    ) : Serializable

    @JsonSerializable
    data class Thumbnails(
            @Json(name = "default")
            val def: Thumbnail,
            val medium: Thumbnail,
            val high: Thumbnail
    ) : Serializable {

        @JsonSerializable
        data class Thumbnail(
                val url: String,
                val width: Int,
                val height: Int
        ) : Serializable
    }
}
