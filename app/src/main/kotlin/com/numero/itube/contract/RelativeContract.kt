package com.numero.itube.contract

import com.numero.itube.api.response.SearchResponse
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IView

interface RelativeContract {

    interface View : IView<Presenter> {
        fun showVideoList(videoList: List<SearchResponse.Video>)

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()
    }

    interface Presenter : IPresenter {
        fun loadRelative(key: String, videoId: String)
    }
}