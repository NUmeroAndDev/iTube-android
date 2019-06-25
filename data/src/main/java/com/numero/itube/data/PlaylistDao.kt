package com.numero.itube.data

import androidx.room.*
import com.numero.itube.data.entity.PlaylistEntity
import com.numero.itube.data.entity.PlaylistVideo
import com.numero.itube.data.entity.VideoEntity
import com.numero.itube.data.entity.VideoLinkingPlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideo(video: VideoEntity)

    @Query("SELECT Video.*, Playlist.id AS playlistId, Playlist.title AS playlistTitle FROM VideoLinkingPlaylist LEFT JOIN Video ON VideoLinkingPlaylist.videoId = Video.id LEFT JOIN Playlist ON VideoLinkingPlaylist.playlistId = Playlist.id")
    fun findAllVideo(): List<PlaylistVideo>

    @Query("SELECT Video.*, Playlist.id AS playlistId, Playlist.title AS playlistTitle FROM VideoLinkingPlaylist LEFT JOIN Video ON VideoLinkingPlaylist.videoId = Video.id LEFT JOIN Playlist ON VideoLinkingPlaylist.playlistId = Playlist.id WHERE VideoLinkingPlaylist.playlistId = :playlistId")
    fun findVideo(playlistId: Long): List<PlaylistVideo>

    @Query("DELETE FROM Video WHERE id = :id")
    fun deleteVideo(id: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateVideo(video: VideoEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM Playlist")
    fun findAllPlaylist(): List<PlaylistEntity>

    @Query("DELETE FROM Playlist WHERE id = :id")
    fun deletePlaylist(id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlaylistEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideoLinkingPlaylist(videoLinkingPlaylist: VideoLinkingPlaylistEntity)

    @Query("DELETE FROM VideoLinkingPlaylist WHERE videoId = :videoId AND playlistId = :playlistId ")
    fun deleteLinkingPlaylist(videoId: String, playlistId: Long)

    @Query("DELETE FROM VideoLinkingPlaylist WHERE playlistId = :playlistId")
    fun deleteLinkingPlaylistAll(playlistId: Long)
}