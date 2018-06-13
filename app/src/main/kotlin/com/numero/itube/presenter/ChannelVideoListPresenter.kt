package com.numero.itube.presenter

import com.numero.itube.contract.ChannelVideoListContract
import com.numero.itube.repository.IYoutubeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

class ChannelVideoListPresenter(
        private val view: ChannelVideoListContract.View,
        private val youtubeRepository: IYoutubeRepository,
        private val channelId: String) : ChannelVideoListContract.Presenter {

    private var disposable: Disposable? = null

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
        disposable?.apply {
            if (isDisposed.not()) {
                dispose()
            }
        }
    }

    override fun loadChannelDetail(key: String) {
        executeLoadChannelVideo(key, channelId)
    }

    override fun loadNextVideo(key: String, nextPageToken: String) {
        executeLoadChannelVideo(key, channelId, nextPageToken)
    }

    private fun executeLoadChannelVideo(key: String, channelId: String, nextPageToken: String? = null) {
        view.showProgress()
        disposable = youtubeRepository.loadChannelVideo(key, channelId, nextPageToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            view.showVideoList(it.items, it.nextPageToken)
                            view.dismissProgress()
                        },
                        onError = {
                            view.showErrorMessage(it)
                            view.dismissProgress()
                        }
                )
    }
}