package com.numero.itube.data

import android.content.Context
import androidx.room.Room
import com.numero.itube.data.entity.PlaylistEntity
import com.numero.itube.data.entity.VideoEntity
import com.numero.itube.data.entity.VideoLinkingPlaylistEntity

class PlaylistDataSourceImpl(
        context: Context
) : PlaylistDataSource {

    private val playlistDao: PlaylistDao = Room.databaseBuilder(context, PlaylistDatabase::class.java, PLAYLIST_DB_FILE_NAME)
            .build()
            .playlistDao()

    override fun registerVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity) {
        playlistDao.insertVideoLinkingPlaylist(VideoLinkingPlaylistEntity(videoEntity.id, playlistEntity.id))
    }

    override fun unregisterVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity) {
        playlistDao.deleteLinkingPlaylist(videoEntity.id, playlistEntity.id)
    }


    override fun createPlaylist(playlistEntity: PlaylistEntity) {
        playlistDao.insertPlaylist(playlistEntity)
    }

    override fun updatePlaylist(playlistEntity: PlaylistEntity) {
        playlistDao.updatePlaylist(playlistEntity)
    }

    override fun deletePlaylist(playlistEntity: PlaylistEntity) {
        playlistDao.deleteLinkingPlaylistAll(playlistEntity.id)
        playlistDao.deletePlaylist(playlistEntity.id)
    }

    companion object {
        private const val PLAYLIST_DB_FILE_NAME = "playlist.db"
    }
}