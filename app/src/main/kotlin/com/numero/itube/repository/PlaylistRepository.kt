package com.numero.itube.repository

import androidx.lifecycle.LiveData
import com.numero.itube.model.Playlist
import com.numero.itube.model.PlaylistDetail
import com.numero.itube.model.PlaylistId
import com.numero.itube.model.PlaylistList

interface PlaylistRepository {

    fun createPlaylist(playlist: Playlist): LiveData<Playlist>

    fun readPlaylistList(): LiveData<PlaylistList>

    fun readPlaylistDetail(playlistId: PlaylistId): LiveData<PlaylistDetail>

    fun updatePlaylist(playlist: Playlist): LiveData<Playlist>

    fun deletePlaylist(playlist: Playlist): LiveData<Playlist>

}