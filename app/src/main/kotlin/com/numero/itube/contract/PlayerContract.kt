package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter

class PlayerContract {

    interface Presenter : IPresenter {
        fun checkFavorite()

        fun loadVideoAndChannelDetail()

        fun loadFavoriteVideo()

        fun changeFavorite()
    }
}