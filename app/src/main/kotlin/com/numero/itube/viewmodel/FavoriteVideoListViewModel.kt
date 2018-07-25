package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.repository.db.FavoriteVideo

class FavoriteVideoListViewModel : ViewModel(), IErrorViewModel, IProgressViewModel {

    val videoList: MutableLiveData<List<FavoriteVideo>> = MutableLiveData()
    val isShowEmptyMessage: MutableLiveData<Boolean> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val isShowProgress: MutableLiveData<Boolean> = MutableLiveData()
}