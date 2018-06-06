package com.numero.itube.contract

import com.numero.itube.api.response.SearchResponse
import com.numero.itube.model.Thumbnail
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IView

interface ChannelVideoListContract {

    interface View : IView<Presenter> {
        fun showChannelThumbnail(thumbnail: Thumbnail)

        fun showVideoList(videoList: List<SearchResponse.Video>, nextPageToken: String? = null)

        fun showAddedVideoList(videoList: List<SearchResponse.Video>, nextPageToken: String? = null)

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()
    }

    interface Presenter : IPresenter {
        fun loadChannelDetail(key: String)

        fun loadNextVideo(key: String, nextPageToken: String)
    }
}