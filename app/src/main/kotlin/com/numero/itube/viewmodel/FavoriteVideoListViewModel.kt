package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.model.FavoriteVideoList

class FavoriteVideoListViewModel : ViewModel(), IErrorViewModel, IProgressViewModel {

    val videoList: MutableLiveData<FavoriteVideoList> = MutableLiveData()
    val isShowEmptyMessage: MutableLiveData<Boolean> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val isShowProgress: MutableLiveData<Boolean> = MutableLiveData()
}