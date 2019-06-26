package com.numero.itube.data.entity

data class PlaylistVideo(
        val id: String,
        val title: String,
        val channelId: String,
        val channelTitle: String,
        val thumbnailUrl: String,
        val playlistId: Long,
        val playlistTitle: String
)