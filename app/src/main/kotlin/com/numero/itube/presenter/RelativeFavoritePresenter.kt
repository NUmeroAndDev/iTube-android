package com.numero.itube.presenter

import com.numero.itube.contract.RelativeFavoriteContract
import com.numero.itube.repository.IFavoriteVideoRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class RelativeFavoritePresenter(
        private val view: RelativeFavoriteContract.View,
        private val favoriteRepository: IFavoriteVideoRepository) : RelativeFavoriteContract.Presenter {

    private val job = Job()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
        job.cancelChildren()
    }

    override fun loadFavoriteList() {
        executeLoadRelative()
    }

    private fun executeLoadRelative() = async(job + UI) {
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