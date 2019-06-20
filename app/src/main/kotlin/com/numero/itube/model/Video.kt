package com.numero.itube.model

sealed class Video(
        val id: VideoId,
        val thumbnailUrl: ThumbnailUrl,
        val title: String
) {
    class Favorite(
            id: VideoId,
            thumbnailUrl: ThumbnailUrl,
            title: String
    ) : Video(id, thumbnailUrl, title)

    class Search(
            id: VideoId,
            thumbnailUrl: ThumbnailUrl,
            title: String,
            val channelId: String // FIXME
    ) : Video(id, thumbnailUrl, title)
}

inline class VideoId(val value: String)