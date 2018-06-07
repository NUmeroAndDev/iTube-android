package com.numero.itube.contract

import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.VideoDetailResponse

interface BaseRelativeContract {

    interface IBaseRelativeView {
        fun setIsRegistered(isRegistered: Boolean)

        fun registeredFavorite(isRegistered: Boolean)

        fun showVideoDetail(videoDetail: VideoDetailResponse.VideoDetail, channel: ChannelResponse.Channel, channelId: String)
    }

    interface IBaseRelativePresenter {
        fun registerFavorite()

        fun unregisterFavorite()
    }
}