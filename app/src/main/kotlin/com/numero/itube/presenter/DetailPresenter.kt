package com.numero.itube.presenter

import com.numero.itube.contract.DetailContract
import com.numero.itube.model.VideoDetail
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class DetailPresenter(
        private val view: DetailContract.View,
        private val youtubeRepository: IYoutubeRepository,
        private val favoriteRepository: IFavoriteVideoRepository,
        private val videoId: String) : DetailContract.Presenter {

    private var videoDetail: VideoDetail? = null
    private val job = Job()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
        executeCheckFavorite(videoId)
    }

    override fun unSubscribe() {
        job.cancelChildren()
    }

    override fun loadDetail(key: String) {
        executeLoadDetail(key, videoId)
    }

    override fun registerFavorite() {
        val detail = videoDetail ?: return
        executeRegisterFavorite(detail)
    }

    override fun unregisterFavorite() {
        executeUnregisterFavorite(videoId)
    }

    private fun executeCheckFavorite(videoId: String) = async(job + UI) {
        try {
            val isFavorite = favoriteRepository.existFavoriteVideo(videoId).await()
            view.registeredFavorite(isFavorite)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun executeLoadDetail(key: String, id: String) = async(job + UI) {
        view.showProgress()
        try {
            val response = youtubeRepository.loadDetail(key, id).await()
            videoDetail = response.items[0]
            view.showVideoDetail(response.items[0])
        } catch (t: Throwable) {
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }

    private fun executeRegisterFavorite(video: VideoDetail) = async(job + UI) {
        try {
            val favoriteVideo = FavoriteVideo(
                    video.id,
                    video.snippet.publishedAt,
                    video.snippet.title,
                    video.snippet.channelId,
                    video.snippet.channelTitle,
                    video.snippet.thumbnails.high.url)
            favoriteRepository.createFavoriteVideo(favoriteVideo).await()
            view.registeredFavorite(true)
        } catch (t: Throwable) {
            // TODO エラー処理
        }
    }

    private fun executeUnregisterFavorite(videoId: String) = async(job + UI) {
        try {
            favoriteRepository.deleteFavoriteVideo(videoId).await()
            view.registeredFavorite(false)
        } catch (t: Throwable) {
            t.printStackTrace()
            // TODO エラー処理
        }
    }
}