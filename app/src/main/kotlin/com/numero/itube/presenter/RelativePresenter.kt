package com.numero.itube.presenter

import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.contract.RelativeContract
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.viewmodel.RelativeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class RelativePresenter(
        private val viewModel: RelativeViewModel,
        private val youtubeRepository: IYoutubeRepository,
        private val favoriteRepository: IFavoriteVideoRepository,
        private val videoId: String,
        private val channelId: String
) : RelativeContract.Presenter {

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

    override fun loadVideoAndChannelDetail(key: String) {
        val request = RelativeRequest(key, videoId, channelId)
        // FIXME zipに
        youtubeRepository.loadRelative(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.progress.postValue(false)
                            viewModel.isShowError.postValue(false)
                            viewModel.videoDetail.postValue(it.videoDetailResponse.items[0])
                            viewModel.channel.postValue(it.channelResponse.items[0])
                            viewModel.videoList.postValue(it.searchResponse.items)
                        },
                        onError = {
                            viewModel.progress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        }
                )
    }

    override fun registerFavorite() {
        val detail = viewModel.videoDetail.value ?: return
        executeRegisterFavorite(detail)
    }

    override fun unregisterFavorite() {
        executeUnregisterFavorite(videoId)
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