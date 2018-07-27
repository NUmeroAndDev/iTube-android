package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter

class ChannelVideoListContract {

    interface Presenter : IPresenter {
        fun loadChannelVideo()

        fun loadMoreVideo()
    }
}