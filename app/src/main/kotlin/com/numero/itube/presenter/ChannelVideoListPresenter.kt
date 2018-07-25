package com.numero.itube.presenter

import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.contract.ChannelVideoListContract
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.viewmodel.ChannelVideoListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy

class ChannelVideoListPresenter(
        private val viewModel: ChannelVideoListViewModel,
        private val channelId: String,
        private val youtubeRepository: IYoutubeRepository
) : ChannelVideoListContract.Presenter {

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun loadChannelVideo(key: String) {
        viewModel.progress.postValue(true)
        val request = ChannelVideoRequest(key, channelId)
        val stream = Observables.zip(
                youtubeRepository.loadChannelDetail(key, channelId),
                youtubeRepository.loadChannelVideoResponse(request)
        ) { channelDetailResponse, videoResponse ->
            channelDetailResponse to videoResponse
        }
        stream.observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.progress.postValue(false)
                            viewModel.isShowError.postValue(false)

                            val channelDetail = it.first
                            viewModel.channelDetail.postValue(channelDetail.items[0])

                            val videoResponse = it.second
                            viewModel.nextPageToken.postValue(videoResponse.nextPageToken)
                            viewModel.videoList.postValue(videoResponse.videoList)
                            viewModel.hasNextPage.postValue(videoResponse.hasNextPage)
                        },
                        onError = {
                            viewModel.progress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        })
    }

    override fun loadMoreVideo(key: String, nextPageToken: String?) {
        viewModel.progress.postValue(true)
        val request = ChannelVideoRequest(key, channelId, nextPageToken)
        youtubeRepository.loadChannelVideoResponse(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.progress.postValue(false)
                            viewModel.isShowError.postValue(false)

                            viewModel.nextPageToken.postValue(it.nextPageToken)
                            viewModel.videoList.postValue(it.videoList)
                            viewModel.hasNextPage.postValue(it.hasNextPage)
                        },
                        onError = {
                            viewModel.progress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        })
    }
}