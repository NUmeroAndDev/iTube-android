package com.numero.itube.data

import com.numero.itube.data.entity.PlaylistEntity
import com.numero.itube.data.entity.PlaylistPreview
import com.numero.itube.data.entity.PlaylistVideo
import com.numero.itube.data.entity.VideoEntity

interface PlaylistDataSource {

    fun readAllPlaylist(): List<PlaylistEntity>

    fun readAllPlaylistVideo(): List<PlaylistVideo>

    fun readAllPlaylistPreview(): List<PlaylistPreview>

    fun findPlaylistVideo(playlistEntity: PlaylistEntity): List<PlaylistVideo>


    fun registerVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity)

    fun unregisterVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity)


    fun createPlaylist(playlistEntity: PlaylistEntity)

    fun updatePlaylist(playlistEntity: PlaylistEntity)

    fun deletePlaylist(playlistEntity: PlaylistEntity)
}