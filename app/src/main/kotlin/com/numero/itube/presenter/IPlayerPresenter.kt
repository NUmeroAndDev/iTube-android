package com.numero.itube.presenter

interface IPlayerPresenter : IPresenter {
    fun checkFavorite()

    fun loadVideoAndChannelDetail()

    fun loadFavoriteVideo()

    fun changeFavorite()
}