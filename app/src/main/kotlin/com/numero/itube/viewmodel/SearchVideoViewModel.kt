package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.model.Video

class SearchVideoViewModel : ViewModel(), IErrorViewModel, IProgressViewModel {

    var searchWord: String? = null
    val videoList: MutableLiveData<List<Video.Search>> = MutableLiveData()
    val nextPageToken: MutableLiveData<String> = MutableLiveData()
    val hasNextPage: MutableLiveData<Boolean> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val isShowProgress: MutableLiveData<Boolean> = MutableLiveData()
}