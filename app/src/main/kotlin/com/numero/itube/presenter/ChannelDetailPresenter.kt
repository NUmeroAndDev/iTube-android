package com.numero.itube.presenter

import com.numero.itube.contract.ChannelDetailContract
import com.numero.itube.repository.IYoutubeRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class ChannelDetailPresenter(
        private val view: ChannelDetailContract.View,
        private val youtubeRepository: IYoutubeRepository,
        private val channelId: String) : ChannelDetailContract.Presenter {

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
            val channelDetailResponse = youtubeRepository.loadChannelDetail(key, channelId).await()
            val videoResponse = youtubeRepository.loadChannelVideo(key, channelId).await()

            val detail = channelDetailResponse.items[0]

            view.showChannelThumbnail(detail.snippet.thumbnails.high)
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
            val videoResponse = youtubeRepository.loadChannelVideo(key, channelId, nextPageToken).await()

            view.showAddedVideoList(videoResponse.items, videoResponse.nextPageToken)
        } catch (t: Throwable) {
            t.printStackTrace()
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }
}