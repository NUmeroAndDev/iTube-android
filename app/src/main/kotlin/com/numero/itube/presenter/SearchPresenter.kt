package com.numero.itube.presenter

import com.numero.itube.contract.SearchContract
import com.numero.itube.repository.IYoutubeRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class SearchPresenter(
        private val view: SearchContract.View,
        private val youtubeRepository: IYoutubeRepository) : SearchContract.Presenter {

    private val job = Job()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun search(key: String, searchWord: String) {
        executeSearch(key, searchWord)
    }

    private fun executeSearch(key: String, searchWord: String) = async(job + UI) {
        view.showProgress()
        try {
            val response = youtubeRepository.search(key, searchWord).await()
            view.showVideoList(response.items)
        } catch (t: Throwable) {
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }
}