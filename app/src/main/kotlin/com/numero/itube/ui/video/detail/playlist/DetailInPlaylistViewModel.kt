package com.numero.itube.ui.video.detail.playlist

import androidx.lifecycle.*
import com.numero.itube.api.response.Result
import com.numero.itube.extension.zipLiveData
import com.numero.itube.model.*
import com.numero.itube.repository.PlaylistRepository
import com.numero.itube.repository.VideoRepository
import javax.inject.Inject

class DetailInPlaylistViewModel @Inject constructor(
        private val videoRepository: VideoRepository,
        private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val actionLiveData: MutableLiveData<Action<InPlaylistVideoDetailRequest>> = MutableLiveData()
    private val stateLiveData: LiveData<Pair<Result<VideoDetail>, PlaylistDetail>> = actionLiveData.switchMap {
        zipLiveData(
                videoRepository.fetchVideoDetail(it.value.videoId, it.value.channelId),
                playlistRepository.readPlaylistDetail(it.value.playlistId)
        )

    }
    private val _videoDetailLiveData: MediatorLiveData<Pair<VideoDetail,PlaylistDetail>> = MediatorLiveData()
    val videoDetailLiveData: LiveData<Pair<VideoDetail,PlaylistDetail>> = _videoDetailLiveData

    init {
        _videoDetailLiveData.addSource(stateLiveData) {
            val videoDetail = it.first
            if (videoDetail is Result.Success) {
                _videoDetailLiveData.postValue(videoDetail.response to it.second)
            }
        }
    }

    fun executeLoadVideoDetail(videoId: VideoId, channelId: ChannelId, playlistId: PlaylistId) {
        actionLiveData.value = Action(InPlaylistVideoDetailRequest(videoId, channelId, playlistId))
    }

    private data class InPlaylistVideoDetailRequest(
            val videoId: VideoId,
            val channelId: ChannelId,
            val playlistId: PlaylistId
    )
}