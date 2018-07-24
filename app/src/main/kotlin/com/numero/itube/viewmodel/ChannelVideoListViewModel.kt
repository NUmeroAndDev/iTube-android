package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.api.response.SearchResponse

class ChannelVideoListViewModel : ViewModel(), IErrorViewModel, IProgressViewModel {
    val videoList: MutableLiveData<List<SearchResponse.Video>> = MutableLiveData()
    val nextPageToken: MutableLiveData<String> = MutableLiveData()
    val hasNextPage: MutableLiveData<Boolean> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val progress: MutableLiveData<Boolean> = MutableLiveData()
}