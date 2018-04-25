package com.numero.itube.contract

import com.numero.itube.model.VideoDetail
import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IView

interface DetailContract {

    interface View : IView<Presenter> {
        fun showVideoDetail(videoDetail: VideoDetail)

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()

        fun registeredFavorite(isRegistered: Boolean)
    }

    interface Presenter : IPresenter {
        fun loadDetail(key: String)

        fun registerFavorite()

        fun unregisterFavorite()
    }
}