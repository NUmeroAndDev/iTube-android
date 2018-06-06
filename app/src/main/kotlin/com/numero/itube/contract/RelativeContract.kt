package com.numero.itube.contract

import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IErrorHandle
import com.numero.itube.view.IProgressHandle
import com.numero.itube.view.IView

interface RelativeContract {

    interface View : IView<Presenter>, IErrorHandle, IProgressHandle {
        fun showVideoList(videoList: List<SearchResponse.Video>)

        fun showVideoDetail(videoDetail: VideoDetailResponse.VideoDetail, channel: ChannelResponse.Channel)

        fun registeredFavorite(isRegistered: Boolean)
    }

    interface Presenter : IPresenter {
        fun loadRelative(key: String, videoId: String)

        fun loadDetail(key: String)

        fun registerFavorite()

        fun unregisterFavorite()
    }
}