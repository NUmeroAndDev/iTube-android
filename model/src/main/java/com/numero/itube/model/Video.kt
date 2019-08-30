package com.numero.itube.model

sealed class Video(
        val id: VideoId,
        val thumbnailUrl: ThumbnailUrl,
        val title: String,
        val channel: Channel
) {
    class Search(
            id: VideoId,
            thumbnailUrl: ThumbnailUrl,
            title: String,
            channel: Channel
    ) : Video(id, thumbnailUrl, title, channel)

    class InPlaylist(
            id: VideoId,
            thumbnailUrl: ThumbnailUrl,
            title: String,
            channel: Channel,
            val playlistId: PlaylistId
    ) : Video(id, thumbnailUrl, title, channel)
}

inline class VideoId(val value: String)