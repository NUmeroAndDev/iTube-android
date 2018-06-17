package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.response.Response
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.repository.IYoutubeRepository

class ChannelVideoListViewModel(
        private val youtubeRepository: IYoutubeRepository,
        private val channelId: String
) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val requestLiveData = MutableLiveData<ChannelVideoRequest>()
    private val responseLiveData: LiveData<Response<SearchResponse>> = Transformations.switchMap(requestLiveData) {
        youtubeRepository.loadChannelVideoResponse(it)
    }

    val videoList: LiveData<List<SearchResponse.Video>> = Transformations.map(responseLiveData) {
        progress.postValue(false)
        // FIXME ページング
        when (it) {
            is Response.Success -> it.response.items
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
        it.throwable
    }
    override val isShowError: LiveData<Boolean> = Transformations.map(responseLiveData) {
        progress.postValue(false)
        it.throwable != null
    }
    override val progress: MutableLiveData<Boolean> = MutableLiveData()

    fun loadChannelVideo(key: String, nextPageToken: String? = null) {
        val request = ChannelVideoRequest(key, channelId, nextPageToken)
        progress.postValue(true)
        requestLiveData.postValue(request)
    }

//    private fun executeLoadChannelVideo(key: String, channelId: String, nextPageToken: String?) = async(job + UI) {
//        isShowError.postValue(false)
//        progress.postValue(true)
//        try {
//            val request = ChannelVideoRequest(key, channelId, nextPageToken)
//            val videoResponse = youtubeRepository.loadChannelVideo(request).await()
//            val old = videoList.value
//            if (old == null) {
//                videoList.postValue(videoResponse.items)
//            } else {
//                // 既存のリストを追加してリスト全体を返す
//                val list = mutableListOf<SearchResponse.Video>().apply {
//                    addAll(old)
//                    addAll(videoResponse.items)
//                }
//                videoList.postValue(list)
//            }
//            this@ChannelVideoListViewModel.nextPageToken.postValue(videoResponse.nextPageToken)
//        } catch (t: Throwable) {
//            t.printStackTrace()
//            isShowError.postValue(true)
//            error.postValue(t)
//        } finally {
//            progress.postValue(false)
//        }
//    }
}