package com.numero.itube.data

import com.numero.itube.data.entity.PlaylistEntity
import com.numero.itube.data.entity.PlaylistSummaryEntity
import com.numero.itube.data.entity.PlaylistVideo
import com.numero.itube.data.entity.VideoEntity

interface PlaylistDataSource {

    fun readAllPlaylist(): List<PlaylistEntity>

    fun readAllPlaylistVideo(): List<PlaylistVideo>

    fun readAllPlaylistSummary(): List<PlaylistSummaryEntity>

    fun findPlaylistVideo(playlistEntity: PlaylistEntity): List<PlaylistVideo>


    fun registerVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity)

    fun unregisterVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity)


    fun createPlaylist(playlistEntity: PlaylistEntity)

    fun updatePlaylist(playlistEntity: PlaylistEntity)

    fun deletePlaylist(playlistEntity: PlaylistEntity)
}