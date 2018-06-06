package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.IView

interface FavoriteVideoListContract {

    interface View : IView<Presenter> {
        fun showVideoList(videoList: List<FavoriteVideo>)

        fun showEmptyMessage()

        fun hideEmptyMessage()

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()
    }

    interface Presenter : IPresenter
}