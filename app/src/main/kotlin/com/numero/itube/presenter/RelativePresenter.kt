package com.numero.itube.presenter

import com.numero.itube.contract.RelativeContract
import com.numero.itube.model.Video
import com.numero.itube.repository.IYoutubeRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class RelativePresenter(
        private val view: RelativeContract.View,
        private val youtubeRepository: IYoutubeRepository) : RelativeContract.Presenter {

    private val job = Job()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
        job.cancelChildren()
    }

    override fun loadRelative(key: String, video: Video) {
        executeLoadRelative(key, video.id.videoId)
    }

    private fun executeLoadRelative(key: String, id: String) = async(job + UI) {
        view.showProgress()
        try {
            val response = youtubeRepository.loadRelative(key, id).await()
            view.showVideoList(response.items)
        } catch (t: Throwable) {
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }
}