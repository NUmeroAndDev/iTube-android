package com.numero.itube.data

import com.numero.itube.data.entity.PlaylistEntity
import com.numero.itube.data.entity.VideoEntity

interface PlaylistDataSource {
    fun registerVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity)

    fun unregisterVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity)


    fun createPlaylist(playlistEntity: PlaylistEntity)

    fun updatePlaylist(playlistEntity: PlaylistEntity)

    fun deletePlaylist(playlistEntity: PlaylistEntity)
}