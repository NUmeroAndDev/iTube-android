package com.numero.itube.presenter

import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.response.Result
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
                        onNext = { result ->
                            viewModel.isShowProgress.postValue(false)
                            when (result) {
                                is Result.Error -> {
                                    viewModel.isShowError.postValue(true)
                                    viewModel.error.postValue(result.throwable)
                                }
                                is Result.Success -> {
                                    viewModel.isShowError.postValue(false)
                                    viewModel.relativeResponse.postValue(result.response)
                                }
                            }
                        },
                        onError = {
                            viewModel.isShowProgress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        }
                )
    }

    override fun loadNextFavoriteVideo(currentVideoId: String) {
        favoriteRepository.loadFavoriteVideo()
                .map { list ->
                    val position = list.indexOfFirst { it.id == currentVideoId }
                    return@map if (position == list.lastIndex) {
                        list.first()
                    } else {
                        list[position + 1]
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            viewModel.nextFavoriteVideo = it
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
            val response = viewModel.relativeResponse.value ?: return
            val detail = response.videoDetailResponse.items[0]
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