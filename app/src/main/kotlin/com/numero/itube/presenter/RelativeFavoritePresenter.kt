package com.numero.itube.presenter

import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.contract.RelativeFavoriteContract
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren

class RelativeFavoritePresenter(
        private val view: RelativeFavoriteContract.View,
        private val youtubeRepository: IYoutubeRepository,
        private val favoriteRepository: IFavoriteVideoRepository,
        private val videoId: String,
        private val channelId: String) : RelativeFavoriteContract.Presenter {

    private var videoDetail: VideoDetailResponse.VideoDetail? = null
    private val job = Job()
    private var disposable: Disposable? = null

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
        executeCheckFavorite(videoId)
    }

    override fun unSubscribe() {
        job.cancelChildren()
        disposable?.apply {
            if (isDisposed.not()) {
                dispose()
            }
        }
    }

    override fun loadDetail(key: String) {
        executeLoadDetail(key, videoId, channelId)
        executeLoadVideoList()
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

    private fun executeLoadDetail(key: String, id: String, channelId: String) {
        view.hideErrorMessage()
        view.showProgress()
        disposable = Observables.zip(youtubeRepository.loadDetail(key, id), youtubeRepository.loadChannel(key, channelId), { videoDetail, channel ->
            videoDetail to channel
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            videoDetail = it.first.items[0]
                            view.showVideoDetail(it.first.items[0], it.second.items[0], channelId)
                            view.dismissProgress()
                        },
                        onError = {
                            view.showErrorMessage(it)
                            view.dismissProgress()
                        }
                )
    }

    private fun executeLoadVideoList() = async(job + UI) {
        try {
            val list = favoriteRepository.loadFavoriteVideo().await()
            view.showVideoList(list)
        } catch (t: Throwable) {
            view.showErrorMessage(t)
        } finally {
            view.dismissProgress()
        }
    }

    private fun executeRegisterFavorite(video: VideoDetailResponse.VideoDetail) = async(job + UI) {
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