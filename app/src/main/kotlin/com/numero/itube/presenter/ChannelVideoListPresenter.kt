package com.numero.itube.presenter

import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.contract.ChannelVideoListContract
import com.numero.itube.repository.IYoutubeRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class ChannelVideoListPresenter(
        private val view: ChannelVideoListContract.View,
        private val youtubeRepository: IYoutubeRepository,
        private val channelId: String) : ChannelVideoListContract.Presenter {

    private val job = Job()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
        job.cancelChildren()
    }

    override fun loadChannelDetail(key: String) {
        executeLoadChannelDetail(key, channelId)
    }

    override fun loadNextVideo(key: String, nextPageToken: String) {
        executeLoadChannelVideo(key, channelId, nextPageToken)
    }

    private fun executeLoadChannelDetail(key: String, channelId: String) = async(job + UI) {
        view.showProgress()
        try {
            val request = ChannelVideoRequest(key, channelId)
            val videoResponse = youtubeRepository.loadChannelVideo(request).await()
            view.showVideoList(videoResponse.items, videoResponse.nextPageToken)
        } catch (t: Throwable) {
            t.printStackTrace()
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }

    private fun executeLoadChannelVideo(key: String, channelId: String, nextPageToken: String) = async(job + UI) {
        view.showProgress()
        try {
            val request = ChannelVideoRequest(key, channelId, nextPageToken)
            val videoResponse = youtubeRepository.loadChannelVideo(request).await()

            view.showAddedVideoList(videoResponse.items, videoResponse.nextPageToken)
        } catch (t: Throwable) {
            t.printStackTrace()
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }
}