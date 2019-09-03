package com.numero.itube.repository

import androidx.lifecycle.LiveData
import com.numero.itube.model.*

interface PlaylistRepository {

    fun createPlaylist(playlist: Playlist): LiveData<Playlist>

    fun readPlaylistList(): LiveData<PlaylistList>

    fun readPlaylistDetail(playlistId: PlaylistId, currentVideoId: VideoId): LiveData<PlaylistDetail>

    fun readPlaylistSummaryList(): LiveData<PlaylistSummaryList>

    fun updatePlaylist(playlist: Playlist): LiveData<Playlist>

    fun deletePlaylist(playlist: Playlist): LiveData<Playlist>

    fun addVideoToPlaylist(playlist: Playlist, videoDetail: VideoDetail): LiveData<Playlist>
}