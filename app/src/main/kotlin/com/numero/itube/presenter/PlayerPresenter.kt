package com.numero.itube.presenter

import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.repository.IConfigRepository
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.viewmodel.PlayerViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class PlayerPresenter(
        private val viewModel: PlayerViewModel,
        private val youtubeRepository: IYoutubeRepository,
        private val favoriteRepository: IFavoriteVideoRepository,
        private val configRepository: IConfigRepository,
        private val videoId: String,
        private val channelId: String
) : IPlayerPresenter {

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun checkFavorite() {
        favoriteRepository.existFavoriteVideo(videoId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.isFavorite.postValue(it)
                        },
                        onError = {
                        }
                )
    }

    override fun loadVideoAndChannelDetail() {
        if (viewModel.isProcessing) {
            return
        }

        viewModel.isShowProgress.postValue(true)
        viewModel.isShowError.postValue(false)

        val request = RelativeRequest(configRepository.apiKey, videoId, channelId)
        youtubeRepository.loadRelative(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.isShowProgress.postValue(false)
                            viewModel.isShowError.postValue(false)
                            viewModel.videoDetail.postValue(it.videoDetailResponse.items[0])
                            viewModel.channel.postValue(it.channelResponse.items[0])
                            viewModel.relativeVideoList.postValue(it.searchResponse.items)
                        },
                        onError = {
                            viewModel.isShowProgress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        }
                )
    }

    override fun loadFavoriteVideo() {
        favoriteRepository.loadFavoriteVideo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            viewModel.favoriteVideoList.postValue(it)
                        },
                        onError = {
                        }
                )
    }

    override fun changeFavorite() {
        val isFavorite = viewModel.isFavorite.value ?: return
        if (isFavorite) {
            executeUnregisterFavorite(videoId)
        } else {
            val detail = viewModel.videoDetail.value ?: return
            executeRegisterFavorite(detail)
        }
    }

    private fun executeRegisterFavorite(video: VideoDetailResponse.VideoDetail) {
        val favoriteVideo = FavoriteVideo(
                video.id,
                video.snippet.publishedAt,
                video.snippet.title,
                video.snippet.channelId,
                video.snippet.channelTitle,
                video.snippet.thumbnails.high.url)
        favoriteRepository.createFavoriteVideo(favoriteVideo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.isFavorite.postValue(true)
                        },
                        onError = {
                        })
    }

    private fun executeUnregisterFavorite(videoId: String) {
        favoriteRepository.deleteFavoriteVideo(videoId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.isFavorite.postValue(false)
                        },
                        onError = {
                        })
    }
}