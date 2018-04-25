package com.numero.itube.presenter

import com.numero.itube.contract.DetailContract
import com.numero.itube.model.Video
import com.numero.itube.repository.IYoutubeRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class DetailPresenter(
        private val view: DetailContract.View,
        private val youtubeRepository: IYoutubeRepository) : DetailContract.Presenter {

    private val job = Job()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
        job.cancelChildren()
    }

    override fun loadDetail(key: String, video: Video) {
        executeLoadDetail(key, video.id.videoId)
    }

    private fun executeLoadDetail(key: String, id: String) = async(job + UI) {
        view.showProgress()
        try {
            val response = youtubeRepository.loadDetail(key, id).await()
            view.showVideoDetail(response.items[0])
        } catch (t: Throwable) {
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }
}