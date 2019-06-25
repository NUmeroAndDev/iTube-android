package com.numero.itube.model

data class PlaylistVideoList(
        val playlist: Playlist,
        val videoList: List<Video.InPlaylist>
)