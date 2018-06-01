package com.numero.itube.contract

import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.Video
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IView

interface ChannelDetailContract {

    interface View : IView<Presenter> {
        fun showBannerImage(imageUrl: String)

        fun showChannelThumbnail(thumbnail: ChannelDetail.Thumbnails.Thumbnail)

        fun showVideoList(videoList: List<Video>, nextPageToken: String? = null)

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()
    }

    interface Presenter : IPresenter {
        fun loadChannelDetail(key: String)
    }
}