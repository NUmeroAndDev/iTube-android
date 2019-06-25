package com.numero.itube.model

data class Playlist(
        val id: PlaylistId,
        val title: String
)

data class PlaylistList(
        val value: List<Playlist>
)

data class PlaylistDetail(
        val id: PlaylistId,
        val title: String,
        val videoList: List<Video.InPlaylist>
)

inline class PlaylistId(val value: Long)