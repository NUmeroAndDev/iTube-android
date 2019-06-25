package com.numero.itube.model

data class Playlist(
        val id: PlaylistId,
        val title: String
)

inline class PlaylistId(val value: Long)