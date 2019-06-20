package com.numero.itube.model

sealed class Video(
        val id: String,
        val thumbnailUrl: ThumbnailUrl,
        val title: String
) {
    class Favorite(
            id: String,
            thumbnailUrl: ThumbnailUrl,
            title: String
    ) : Video(id, thumbnailUrl, title)

    class Search(
            id: String,
            thumbnailUrl: ThumbnailUrl,
            title: String,
            val channelId: String // FIXME
    ) : Video(id, thumbnailUrl, title)
}