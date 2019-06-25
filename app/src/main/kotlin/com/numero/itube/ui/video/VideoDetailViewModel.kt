package com.numero.itube.ui.video

import androidx.lifecycle.*
import com.numero.itube.api.response.Result
import com.numero.itube.model.Action
import com.numero.itube.model.ChannelId
import com.numero.itube.model.VideoDetail
import com.numero.itube.model.VideoId
import com.numero.itube.repository.VideoRepository
import javax.inject.Inject

class VideoDetailViewModel @Inject constructor(
        private val videoRepository: VideoRepository
) : ViewModel() {

    private val actionLiveData: MutableLiveData<Action<VideoDetailRequest>> = MutableLiveData()
    private val stateLiveData: LiveData<Result<VideoDetail>> = actionLiveData.switchMap {
        videoRepository.fetchVideoDetail(it.value.videoId, it.value.channelId)
    }
    private val _videoDetailLiveData: MediatorLiveData<VideoDetail> = MediatorLiveData()

    val videoDetailLiveData: LiveData<VideoDetail> = _videoDetailLiveData

    init {
        _videoDetailLiveData.addSource(stateLiveData) {
            if (it is Result.Success) {
                _videoDetailLiveData.postValue(it.response)
            }
        }
    }

    fun executeLoadVideoDetail(videoId: VideoId, channelId: ChannelId) {
        actionLiveData.value = Action(VideoDetailRequest(videoId, channelId))
    }

    data class VideoDetailRequest(
            val videoId: VideoId,
            val channelId: ChannelId
    )
}