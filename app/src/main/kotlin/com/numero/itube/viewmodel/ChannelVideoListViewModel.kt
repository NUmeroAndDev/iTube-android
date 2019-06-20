package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.Video

class ChannelVideoListViewModel : ViewModel(), IErrorViewModel, IProgressViewModel {
    val videoList: MutableLiveData<List<Video.Search>> = MutableLiveData()
    val nextPageToken: MutableLiveData<String> = MutableLiveData()
    val hasNextPage: MutableLiveData<Boolean> = MutableLiveData()
    val channelDetail: MutableLiveData<ChannelDetail> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val isShowProgress: MutableLiveData<Boolean> = MutableLiveData()
}