package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter

class PlayerContract {

    interface Presenter : IPresenter {
        fun checkFavorite()

        fun loadVideoAndChannelDetail(key: String)

        fun loadFavoriteVideo()

        fun registerFavorite()

        fun unregisterFavorite()
    }
}