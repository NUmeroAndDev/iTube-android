package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse

class RelativeViewModel: ViewModel(), IErrorViewModel, IProgressViewModel {

    val videoList: MutableLiveData<List<SearchResponse.Video>> = MutableLiveData()
    val videoDetail: MutableLiveData<VideoDetailResponse.VideoDetail> = MutableLiveData()
    val isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val channel: MutableLiveData<ChannelResponse.Channel> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val progress: MutableLiveData<Boolean> = MutableLiveData()
}