package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.IErrorHandle
import com.numero.itube.view.IProgressHandle
import com.numero.itube.view.IView

interface RelativeFavoriteContract {

    interface View : IView<Presenter>, IErrorHandle, IProgressHandle {
        fun showVideoList(videoList: List<FavoriteVideo>)
    }

    interface Presenter : IPresenter {
        fun loadFavoriteList()
    }
}