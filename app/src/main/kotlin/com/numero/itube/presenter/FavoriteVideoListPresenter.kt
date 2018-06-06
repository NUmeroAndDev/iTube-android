package com.numero.itube.presenter

import com.numero.itube.contract.FavoriteVideoListContract
import com.numero.itube.repository.IFavoriteVideoRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class FavoriteVideoListPresenter(
        private val view: FavoriteVideoListContract.View,
        private val favoriteRepository: IFavoriteVideoRepository) : FavoriteVideoListContract.Presenter {

    private val job = Job()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
        executeLoadFavorite()
    }

    override fun unSubscribe() {
        job.cancelChildren()
    }

    private fun executeLoadFavorite() = async(job + UI) {
        view.hideEmptyMessage()
        view.showProgress()
        try {
            val list = favoriteRepository.loadFavoriteVideo().await()
            if (list.isEmpty()) {
                view.showEmptyMessage()
            } else {
                view.showVideoList(list)
            }
        } catch (t: Throwable) {
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }
}