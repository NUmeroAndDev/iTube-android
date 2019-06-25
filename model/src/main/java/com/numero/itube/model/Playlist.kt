package com.numero.itube.model

data class Playlist(
        val id: PlaylistId,
        val title: String,
        val videoList: List<Video.InPlaylist>
)

data class PlaylistList(
        val value: List<Playlist>
)

inline class PlaylistId(val value: Long)