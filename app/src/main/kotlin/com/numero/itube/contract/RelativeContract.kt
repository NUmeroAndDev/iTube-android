package com.numero.itube.contract

import com.numero.itube.api.response.SearchResponse
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IErrorHandle
import com.numero.itube.view.IProgressHandle
import com.numero.itube.view.IView

interface RelativeContract {

    interface View : IView<Presenter>, IErrorHandle, IProgressHandle {
        fun showVideoList(videoList: List<SearchResponse.Video>)
    }

    interface Presenter : IPresenter {
        fun loadRelative(key: String, videoId: String)
    }
}