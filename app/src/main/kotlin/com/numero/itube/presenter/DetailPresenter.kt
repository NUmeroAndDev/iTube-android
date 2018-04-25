package com.numero.itube.presenter

import com.numero.itube.contract.DetailContract
import com.numero.itube.model.Video
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
        private val video: Video) : DetailContract.Presenter {

    private val job = Job()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
        executeCheckFavorite(video)
    }

    override fun unSubscribe() {
        job.cancelChildren()
    }

    override fun loadDetail(key: String) {
        executeLoadDetail(key, video.id.videoId)
    }

    override fun registerFavorite() {
        executeRegisterFavorite(video)
    }

    override fun unregisterFavorite() {
        executeUnregisterFavorite(video)
    }

    private fun executeCheckFavorite(video: Video) = async(job + UI) {
        try {
            val isFavorite = favoriteRepository.existFavoriteVideo(video.id.videoId).await()
            view.registeredFavorite(isFavorite)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
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

    private fun executeRegisterFavorite(video: Video) = async(job + UI) {
        try {
            val favoriteVideo = FavoriteVideo(
                    video.id.videoId,
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

    private fun executeUnregisterFavorite(video: Video) = async(job + UI) {
        try {
            favoriteRepository.deleteFavoriteVideo(video.id.videoId).await()
            view.registeredFavorite(false)
        } catch (t: Throwable) {
            // TODO エラー処理
        }
    }
}