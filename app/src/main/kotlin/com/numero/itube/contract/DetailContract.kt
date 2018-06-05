package com.numero.itube.contract

import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IView

interface DetailContract {

    interface View : IView<Presenter> {
        fun showVideoDetail(videoDetail: VideoDetailResponse.VideoDetail, channel: ChannelResponse.Channel)

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()

        fun registeredFavorite(isRegistered: Boolean)
    }

    interface Presenter : IPresenter {
        fun loadDetail(key: String)

        fun registerFavorite()

        fun unregisterFavorite()
    }
}