package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.response.Response
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoResponse
import com.numero.itube.repository.IYoutubeRepository

class ChannelVideoListViewModel(
        private val youtubeRepository: IYoutubeRepository,
        private val channelId: String
) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val requestLiveData = MutableLiveData<ChannelVideoRequest>()
    private val responseLiveData: LiveData<Response<VideoResponse>> = Transformations.switchMap(requestLiveData) {
        youtubeRepository.loadChannelVideoResponse(it)
    }

    val videoList: LiveData<List<SearchResponse.Video>> = Transformations.map(responseLiveData) {
        when (it) {
            is Response.Success -> it.response.videoList
            else -> null
        }
    }
    val nextPageToken: LiveData<String> = Transformations.map(responseLiveData) {
        when (it) {
            is Response.Success -> it.response.nextPageToken
            else -> null
        }
    }

    override val error: LiveData<Throwable> = Transformations.map(responseLiveData) {
        if (it is Response.Error) {
            it.throwable
        }
        null
    }
    override val isShowError: LiveData<Boolean> = Transformations.map(responseLiveData) {
        it is Response.Error
    }
    override val progress: LiveData<Boolean> = youtubeRepository.isProgressLiveData

    fun loadChannelVideo(key: String, nextPageToken: String? = null) {
        val request = ChannelVideoRequest(key, channelId, nextPageToken)
        requestLiveData.postValue(request)
    }
}