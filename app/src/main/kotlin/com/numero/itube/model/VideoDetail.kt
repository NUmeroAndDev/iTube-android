package com.numero.itube.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class VideoDetail(
        val kind: String,
        val etag: String,
        val id: String,
        val snippet: Snippet
) : Serializable {

    @JsonSerializable
    data class Snippet(
            val publishedAt: String,
            val channelId: String,
            val title: String,
            val description: String,
            val thumbnails: Thumbnails,
            val channelTitle: String,
            val categoryId: String,
            val liveBroadcastContent: String
    ) : Serializable

    @JsonSerializable
    data class Thumbnails(
            @Json(name = "default")
            val def: Thumbnail,
            val medium: Thumbnail,
            val high: Thumbnail,
            val standard: Thumbnail
    ) : Serializable {

        @JsonSerializable
        data class Thumbnail(
                val url: String,
                val width: Int,
                val height: Int
        ) : Serializable
    }
}
