package com.numero.itube.presenter

import com.numero.itube.contract.FavoriteContract
import com.numero.itube.repository.IFavoriteVideoRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class FavoritePresenter(
        private val view: FavoriteContract.View,
        private val favoriteRepository: IFavoriteVideoRepository) : FavoriteContract.Presenter {

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
        view.showProgress()
        try {
            val list = favoriteRepository.loadFavoriteVideo().await()
            view.showVideoList(list)
        } catch (t: Throwable) {
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }
}