package com.numero.itube.repository

import androidx.lifecycle.LiveData
import com.numero.itube.model.Playlist
import com.numero.itube.model.PlaylistId
import com.numero.itube.model.PlaylistList

interface PlaylistRepository {

    fun createPlaylist(playlist: Playlist): LiveData<Playlist>

    fun readPlaylistList(): LiveData<PlaylistList>

    fun readPlaylist(playlistId: PlaylistId): LiveData<Playlist>

    fun updatePlaylist(playlist: Playlist): LiveData<Playlist>

    fun deletePlaylist(playlist: Playlist): LiveData<Playlist>

}