package com.numero.itube.api.response

import com.numero.itube.model.PageInfo
import com.numero.itube.model.Thumbnail
import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class VideoDetailResponse(
        val kind: String,
        val etag: String,
        val pageInfo: PageInfo,
        val items: List<VideoDetail>
) : Serializable {

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
                val standard: Thumbnail?
        ) : Serializable
    }
}