package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter

class ChannelVideoListContract {

    interface Presenter : IPresenter {
        fun loadChannelVideo(key: String, channelId: String, nextPageToken: String? = null)
    }
}