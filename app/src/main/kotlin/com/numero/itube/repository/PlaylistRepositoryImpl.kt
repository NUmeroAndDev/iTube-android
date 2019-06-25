package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.numero.itube.data.PlaylistDataSource
import com.numero.itube.data.entity.PlaylistEntity
import com.numero.itube.data.entity.PlaylistVideo
import com.numero.itube.data.entity.VideoEntity
import com.numero.itube.model.*
import kotlinx.coroutines.Dispatchers

class PlaylistRepositoryImpl(
        private val playlistDataSource: PlaylistDataSource
) : PlaylistRepository {

    override fun createPlaylist(playlist: Playlist): LiveData<Playlist> = liveData(Dispatchers.IO) {
        playlistDataSource.createPlaylist(playlist.toEntity())
        emit(playlist)
    }

    override fun readPlaylistList(): LiveData<PlaylistList> = liveData(Dispatchers.IO) {
        val playlist = playlistDataSource.readAllPlaylist()
        val list = playlist.toPlaylist()
        emit(PlaylistList(list))
    }

    override fun readPlaylistDetail(playlistId: PlaylistId): LiveData<PlaylistDetail> = liveData(Dispatchers.IO) {
        val videoList = playlistDataSource.findPlaylistVideo(PlaylistEntity(playlistId.value, ""))
        val playlist = PlaylistDetail(
                playlistId,
                videoList.first().playlistTitle,
                videoList.toVideo()
        )
        emit(playlist)
    }

    override fun updatePlaylist(playlist: Playlist): LiveData<Playlist> = liveData(Dispatchers.IO) {
        playlistDataSource.updatePlaylist(playlist.toEntity())
        emit(playlist)
    }

    override fun deletePlaylist(playlist: Playlist): LiveData<Playlist> = liveData(Dispatchers.IO) {
        playlistDataSource.deletePlaylist(playlist.toEntity())
        emit(playlist)
    }

    override fun addVideoToPlaylist(playlist: Playlist, videoDetail: VideoDetail): LiveData<Playlist> = liveData(Dispatchers.IO) {
        playlistDataSource.registerVideo(videoDetail.toEntity(), playlist.toEntity())
        emit(playlist)
    }

    private fun Playlist.toEntity(): PlaylistEntity {
        return PlaylistEntity(id.value, title)
    }

    private fun List<PlaylistEntity>.toPlaylist(): List<Playlist> {
        return map {
            Playlist(
                    PlaylistId(it.id),
                    it.title
            )
        }
    }

    private fun List<PlaylistVideo>.toVideo(): List<Video.InPlaylist> {
        return map {
            Video.InPlaylist(
                    VideoId(it.id),
                    ThumbnailUrl(it.thumbnailUrl),
                    it.title,
                    Channel(
                            ChannelId(it.channelId),
                            it.channelTitle
                    ),
                    PlaylistId(it.playlistId)
            )
        }
    }

    private fun VideoDetail.toEntity(): VideoEntity {
        return VideoEntity(
                videoId.value,
                title,
                channelDetail.id.value,
                channelDetail.title,
                thumbnailUrl.value
        )
    }
}