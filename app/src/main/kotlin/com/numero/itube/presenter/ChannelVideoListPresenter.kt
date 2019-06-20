package com.numero.itube.presenter

import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.repository.IConfigRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.viewmodel.ChannelVideoListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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

        GlobalScope.launch(Dispatchers.Main) {
            val channelDetailResult = async(Dispatchers.Default) { youtubeRepository.loadChannelDetail(configRepository.apiKey, channelId) }.await()
            val channelVideoResult = async(Dispatchers.Default) { youtubeRepository.loadChannelVideo(request) }.await()

            viewModel.isShowProgress.postValue(false)
            if (channelDetailResult is Result.Success && channelVideoResult is Result.Success) {
                val channelDetail = channelDetailResult.response
                viewModel.channelDetail.postValue(channelDetail)

                val response = channelVideoResult.response
                viewModel.nextPageToken.postValue(response.nextPageToken)
                viewModel.videoList.postValue(response.videoList)
                viewModel.hasNextPage.postValue(response.hasNextPage)
            } else {
                viewModel.isShowError.postValue(true)
                viewModel.error.postValue(null)
            }
        }
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

        GlobalScope.launch(Dispatchers.Main) {
            val result = async(Dispatchers.Default) { youtubeRepository.loadChannelVideo(request) }.await()

            viewModel.isShowProgress.postValue(false)
            viewModel.isShowError.postValue(result is Result.Error)

            when (result) {
                is Result.Error -> viewModel.error.postValue(result.throwable)
                is Result.Success -> {
                    viewModel.isShowError.postValue(false)
                    val response = result.response
                    viewModel.nextPageToken.postValue(response.nextPageToken)
                    viewModel.videoList.postValue(response.videoList)
                    viewModel.hasNextPage.postValue(response.hasNextPage)
                }
            }
        }
    }
}