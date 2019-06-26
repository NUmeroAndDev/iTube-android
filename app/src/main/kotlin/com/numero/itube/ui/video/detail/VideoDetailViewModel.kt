package com.numero.itube.ui.video.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.numero.itube.model.Action
import com.numero.itube.model.Playlist
import com.numero.itube.model.VideoDetail
import com.numero.itube.repository.PlaylistRepository
import com.numero.itube.repository.VideoRepository
import javax.inject.Inject

class VideoDetailViewModel @Inject constructor(
        private val videoRepository: VideoRepository,
        private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val addPlaylistActionLiveData: MutableLiveData<Action<AddPlaylistAction>> = MutableLiveData()
    val addedPlaylistLiveData: LiveData<Playlist> = addPlaylistActionLiveData.switchMap {
        playlistRepository.addVideoToPlaylist(it.value.playlist, it.value.videoDetail)
    }

    fun executeAddPlaylist(targetPlaylist: Playlist, videoDetail: VideoDetail) {
        addPlaylistActionLiveData.value = Action(AddPlaylistAction(targetPlaylist, videoDetail))
    }

    data class AddPlaylistAction(
            val playlist: Playlist,
            val videoDetail: VideoDetail
    )
}