package com.numero.itube.contract

import com.numero.itube.model.Video
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IView

interface SearchContract {

    interface View : IView<Presenter> {
        fun showVideoList(videoList: List<Video>, nextPageToken: String? = null)

        fun addVideoList(videoList: List<Video>, nextPageToken: String? = null)

        fun clearVideoList()

        fun showEmptyMessage()

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()
    }

    interface Presenter : IPresenter {
        fun search(key: String, searchWord: String, nestPageToken: String? = null)
    }
}