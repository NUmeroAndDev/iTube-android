package com.numero.itube.contract

import com.numero.itube.api.response.SearchResponse
import com.numero.itube.model.Thumbnail
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IErrorHandle
import com.numero.itube.view.IProgressHandle
import com.numero.itube.view.IView

interface ChannelVideoListContract {

    interface View : IView<Presenter>, IErrorHandle, IProgressHandle {
        fun showChannelThumbnail(thumbnail: Thumbnail)

        fun showVideoList(videoList: List<SearchResponse.Video>, nextPageToken: String? = null)

        fun showAddedVideoList(videoList: List<SearchResponse.Video>, nextPageToken: String? = null)
    }

    interface Presenter : IPresenter {
        fun loadChannelDetail(key: String)

        fun loadNextVideo(key: String, nextPageToken: String)
    }
}