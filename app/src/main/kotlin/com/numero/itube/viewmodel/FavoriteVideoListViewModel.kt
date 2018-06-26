package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.db.FavoriteVideo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class FavoriteVideoListViewModel(private val favoriteRepository: IFavoriteVideoRepository) : ViewModel(), IErrorViewModel, IProgressViewModel {

    val videoList: MutableLiveData<List<FavoriteVideo>> = MutableLiveData()
    val isShowEmptyMessage: MutableLiveData<Boolean> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val progress: MutableLiveData<Boolean> = MutableLiveData()

    fun loadFavoriteVideoList() {
        executeLoadFavorite()
    }

    private fun executeLoadFavorite() {
        progress.postValue(true)
        favoriteRepository.loadFavoriteVideo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            isShowEmptyMessage.postValue(it.isEmpty())
                            progress.postValue(false)
                            videoList.postValue(it)
                        },
                        onError = {
                            isShowEmptyMessage.postValue(false)
                            progress.postValue(false)
                            error.postValue(it)
                        }
                )
    }
}