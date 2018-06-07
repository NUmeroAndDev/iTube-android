package com.numero.itube.contract

import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.presenter.IPresenter
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.IErrorHandle
import com.numero.itube.view.IProgressHandle
import com.numero.itube.view.IView

interface RelativeFavoriteContract {

    interface View : IView<Presenter>, IErrorHandle, IProgressHandle {
        fun showVideoList(videoList: List<FavoriteVideo>)

        fun showVideoDetail(videoDetail: VideoDetailResponse.VideoDetail, channel: ChannelResponse.Channel)

        fun registeredFavorite(isRegistered: Boolean)
    }

    interface Presenter : IPresenter {
        fun loadDetail(key: String)

        fun registerFavorite()

        fun unregisterFavorite()
    }
}