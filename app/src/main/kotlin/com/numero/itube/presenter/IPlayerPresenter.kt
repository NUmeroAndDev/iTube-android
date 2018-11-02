package com.numero.itube.presenter

interface IPlayerPresenter : IPresenter {
    fun checkFavorite()

    fun loadVideoAndChannelDetail()

    fun loadNextFavoriteVideo(currentVideoId: String)

    fun changeFavorite()
}