package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class FavoriteVideoListViewModel(private val favoriteRepository: IFavoriteVideoRepository) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val job = Job()

    val videoList: MutableLiveData<List<FavoriteVideo>> = MutableLiveData()
    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val progress: MutableLiveData<Boolean> = MutableLiveData()

    fun loadFavoriteVideoList() {
        executeLoadFavorite()
    }

    private fun executeLoadFavorite() = async(job + UI) {
        progress.postValue(true)
        try {
            val list = favoriteRepository.loadFavoriteVideo().await()
//            if (list.isEmpty()) {
//                view.showEmptyMessage()
//            } else {
//                view.showVideoList(list)
//            }
            videoList.postValue(list)
        } catch (t: Throwable) {
            error.postValue(t)
        } finally {
            progress.postValue(false)
        }
    }
}