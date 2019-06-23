package com.numero.itube.viewmodel

import androidx.lifecycle.*
import com.numero.itube.api.response.Result
import com.numero.itube.model.*
import com.numero.itube.repository.VideoRepository
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
        private val videoRepository: VideoRepository
) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val actionLiveData: MutableLiveData<Action<VideoDetailRequest>> = MutableLiveData()
    private val stateLiveData: LiveData<Result<VideoDetail>> = actionLiveData.switchMap {
        videoRepository.fetchVideoDetail(it.value.videoId, it.value.channelId)
    }
    private val _videoDetailLiveData: MediatorLiveData<VideoDetail> = MediatorLiveData()

    val videoDetailLiveData: LiveData<VideoDetail> = _videoDetailLiveData

    var nextFavoriteVideo: Video.Favorite? = null
    val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val isShowProgress: MutableLiveData<Boolean> = MutableLiveData()

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