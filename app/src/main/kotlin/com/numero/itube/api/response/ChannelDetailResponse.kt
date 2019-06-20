package com.numero.itube.api.response

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class ChannelDetailResponse(
        val kind: String,
        val etag: String,
        val items: List<ChannelDetail>
) : Serializable {

    @JsonSerializable
    data class ChannelDetail(
            val kind: String,
            val etag: String,
            val id: String,
            val snippet: Snippet,
            @Json(name = "brandingSettings")
            val branding: Branding
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
        ) : Serializable

        @JsonSerializable
        data class Branding(
                val channel: Channel,
                val image: Image
        ) : Serializable {

            @JsonSerializable
            data class Channel(
                    val title: String,
                    val description: String?
            ) : Serializable

            @JsonSerializable
            data class Image(
                    val bannerMobileImageUrl: String,
                    val bannerMobileLowImageUrl: String,
                    val bannerMobileMediumHdImageUrl: String,
                    val bannerMobileHdImageUrl: String,
                    val bannerMobileExtraHdImageUrl: String,
                    val bannerTvMediumImageUrl: String
            ) : Serializable
        }

    }

}