package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter

class RelativeContract {

    interface Presenter : IPresenter {
        fun checkFavorite()

        fun loadVideoAndChannelDetail(key: String)

        fun registerFavorite()

        fun unregisterFavorite()
    }
}