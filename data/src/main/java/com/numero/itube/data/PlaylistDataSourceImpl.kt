package com.numero.itube.data

import android.content.Context
import androidx.room.Room
import com.numero.itube.data.entity.*

class PlaylistDataSourceImpl(
        context: Context
) : PlaylistDataSource {

    private val playlistDao: PlaylistDao = Room.databaseBuilder(context, PlaylistDatabase::class.java, PLAYLIST_DB_FILE_NAME)
            .build()
            .playlistDao()

    override fun readAllPlaylist(): List<PlaylistEntity> {
        return playlistDao.findAllPlaylist()
    }

    override fun readAllPlaylistVideo(): List<PlaylistVideo> {
        return playlistDao.findAllVideo()
    }

    override fun readAllPlaylistPreview(): List<PlaylistPreview> {
        return playlistDao.findAllPlaylistPreview()
    }

    override fun findPlaylistVideo(playlistEntity: PlaylistEntity): List<PlaylistVideo> {
        return playlistDao.findVideo(playlistEntity.id)
    }

    override fun registerVideo(videoEntity: VideoEntity, playlistEntity: PlaylistEntity) {
        val link = VideoLinkingPlaylistEntity(
                videoId = videoEntity.id,
                playlistId = playlistEntity.id
        )
        playlistDao.insertVideoLinkingPlaylist(link)
        playlistDao.insertVideo(videoEntity)
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