package com.numero.itube.model

data class Playlist(
        val id: PlaylistId,
        val title: String
) {
    companion object {
        fun createPlaylist(title: String): Playlist {
            return Playlist(PlaylistId(0), title)
        }
    }
}

data class PlaylistList(
        val value: List<Playlist>
)

data class PlaylistSummaryList(
        val value: List<PlaylistSummary>
)

data class PlaylistSummary(
        val id: PlaylistId,
        val title: String,
        val totalVideoCount: Int,
        val video: Video.InPlaylist?
)

data class PlaylistDetail(
        val id: PlaylistId,
        val title: String,
        val videoList: List<Video.InPlaylist>
)

data class PlaylistDetailList(
        val value: List<PlaylistDetail>
)

inline class PlaylistId(val value: Long)