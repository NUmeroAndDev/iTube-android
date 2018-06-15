package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.repository.IYoutubeRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class ChannelVideoListViewModel(
        private val youtubeRepository: IYoutubeRepository,
        private val channelId: String
) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val job = Job()

    val videoList: MutableLiveData<List<SearchResponse.Video>> = MutableLiveData()
    val nextPageToken: MutableLiveData<String> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val progress: MutableLiveData<Boolean> = MutableLiveData()

    fun loadChannelVideo(key: String, nextPageToken: String? = null) {
        executeLoadChannelVideo(key, channelId, nextPageToken)
    }

    private fun executeLoadChannelVideo(key: String, channelId: String, nextPageToken: String?) = async(job + UI) {
        isShowError.postValue(false)
        progress.postValue(true)
        try {
            val request = ChannelVideoRequest(key, channelId, nextPageToken)
            val videoResponse = youtubeRepository.loadChannelVideo(request).await()
            videoList.postValue(videoResponse.items)
            this@ChannelVideoListViewModel.nextPageToken.postValue(videoResponse.nextPageToken)
        } catch (t: Throwable) {
            t.printStackTrace()
            isShowError.postValue(true)
            error.postValue(t)
        } finally {
            progress.postValue(false)
        }
    }
}