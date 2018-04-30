package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.IView

interface RelativeFavoriteContract {

    interface View : IView<Presenter> {
        fun showVideoList(videoList: List<FavoriteVideo>)

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()
    }

    interface Presenter : IPresenter {
        fun loadFavoriteList()
    }
}