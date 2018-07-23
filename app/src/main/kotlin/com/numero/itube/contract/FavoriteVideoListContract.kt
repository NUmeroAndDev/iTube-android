package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter

class FavoriteVideoListContract {

    interface Presenter : IPresenter {
        fun loadFavoriteVideoList()
    }
}