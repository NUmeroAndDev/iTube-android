package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.repository.db.FavoriteVideo

class PlayerViewModel : ViewModel(), IErrorViewModel, IProgressViewModel {

    val favoriteVideoList: MutableLiveData<List<FavoriteVideo>> = MutableLiveData()
    val relativeResponse: MutableLiveData<RelativeResponse> = MutableLiveData()
    val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val isShowProgress: MutableLiveData<Boolean> = MutableLiveData()
}