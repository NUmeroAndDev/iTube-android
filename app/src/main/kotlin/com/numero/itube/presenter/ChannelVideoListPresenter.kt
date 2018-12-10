package com.numero.itube.presenter

import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.repository.IConfigRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.viewmodel.ChannelVideoListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy

class ChannelVideoListPresenter(
        private val viewModel: ChannelVideoListViewModel,
        private val channelId: String,
        private val youtubeRepository: IYoutubeRepository,
        private val configRepository: IConfigRepository
) : IChannelVideoListPresenter {

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun loadChannelVideo() {
        if (viewModel.isProcessing) {
            return
        }

        viewModel.isShowProgress.postValue(true)
        val request = ChannelVideoRequest(configRepository.apiKey, channelId)
        val stream = Observables.zip(
                youtubeRepository.loadChannelDetail(configRepository.apiKey, channelId),
                youtubeRepository.loadChannelVideoResponse(request)
        ) { channelDetailResponse, videoResponse ->
            channelDetailResponse to videoResponse
        }
        stream.observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.isShowProgress.postValue(false)

                            val channelDetailResult = it.first
                            when (channelDetailResult) {
                                is Result.Error -> {
                                    viewModel.isShowError.postValue(true)
                                    viewModel.error.postValue(channelDetailResult.throwable)
                                }
                                is Result.Success -> {
                                    val response = channelDetailResult.response
                                    viewModel.channelDetail.postValue(response.items[0])
                                }
                            }

                            val videoResult = it.second
                            when (videoResult) {
                                is Result.Error -> {
                                    viewModel.isShowError.postValue(true)
                                    viewModel.error.postValue(videoResult.throwable)
                                }
                                is Result.Success -> {
                                    val response = videoResult.response
                                    viewModel.nextPageToken.postValue(response.nextPageToken)
                                    viewModel.videoList.postValue(response.videoList)
                                    viewModel.hasNextPage.postValue(response.hasNextPage)
                                }
                            }
                        },
                        onError = {
                            viewModel.isShowProgress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        })
    }

    override fun loadMoreVideo() {
        val hasNextPage = viewModel.hasNextPage.value
        if (hasNextPage != null && hasNextPage.not()) {
            return
        }
        val nextPageToken = viewModel.nextPageToken.value
        if (viewModel.isProcessing) {
            return
        }

        viewModel.isShowProgress.postValue(true)
        val request = ChannelVideoRequest(configRepository.apiKey, channelId, nextPageToken)
        youtubeRepository.loadChannelVideoResponse(request)
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
                                    val response = result.response
                                    viewModel.nextPageToken.postValue(response.nextPageToken)
                                    viewModel.videoList.postValue(response.videoList)
                                    viewModel.hasNextPage.postValue(response.hasNextPage)
                                }
                            }
                        },
                        onError = {
                            viewModel.isShowProgress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        })
    }
}