package com.numero.itube.contract

import com.numero.itube.api.response.SearchResponse
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IErrorHandle
import com.numero.itube.view.IProgressHandle
import com.numero.itube.view.IView

interface SearchContract {

    interface View : IView<Presenter>, IErrorHandle, IProgressHandle {
        fun showVideoList(videoList: List<SearchResponse.Video>, nextPageToken: String? = null)

        fun addVideoList(videoList: List<SearchResponse.Video>, nextPageToken: String? = null)

        fun clearVideoList()

        fun showEmptyMessage()
    }

    interface Presenter : IPresenter {
        fun search(key: String, searchWord: String, nestPageToken: String? = null)
    }
}