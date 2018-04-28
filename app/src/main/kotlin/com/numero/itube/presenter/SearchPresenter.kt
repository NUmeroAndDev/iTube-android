package com.numero.itube.presenter

import com.numero.itube.contract.SearchContract
import com.numero.itube.repository.IYoutubeRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

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
        job.cancelChildren()
    }

    override fun search(key: String, searchWord: String, nestPageToken: String?) {
        executeSearch(key, searchWord, nestPageToken)
    }

    private fun executeSearch(key: String, searchWord: String, nestPageToken: String?) = async(job + UI) {
        view.showProgress()
        try {
            val response = youtubeRepository.search(key, searchWord, nestPageToken).await()
            if (nestPageToken == null) {
                view.showVideoList(response.items, response.nextPageToken)
            } else {
                view.addVideoList(response.items, response.nextPageToken)
            }
        } catch (t: Throwable) {
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }
}