package com.numero.itube.api.response

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class SearchResponse(
        val kind: String,
        val etag: String,
        val nextPageToken: String?,
        val regionCode: String,
        val pageInfo: PageInfo,
        var items: List<Video>
) : Serializable {

    @JsonSerializable
    data class Video(
            val kind: String,
            val etag: String,
            val id: Id,
            val snippet: Snippet
    ) : Serializable {

        @JsonSerializable
        data class Id(
                val kind: String,
                val videoId: String
        ) : Serializable

        @JsonSerializable
        data class Snippet(
                val publishedAt: String,
                val channelId: String,
                val title: String,
                val description: String,
                val thumbnails: Thumbnails,
                val channelTitle: String,
                val liveBroadcastContent: String
        ) : Serializable

        @JsonSerializable
        data class Thumbnails(
                @Json(name = "default")
                val def: Thumbnail,
                val medium: Thumbnail,
                val high: Thumbnail
        ) : Serializable
    }
}