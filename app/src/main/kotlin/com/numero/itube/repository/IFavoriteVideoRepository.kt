package com.numero.itube.repository

import com.numero.itube.repository.db.FavoriteVideo
import io.reactivex.Flowable
import io.reactivex.Maybe

interface IFavoriteVideoRepository {
    fun createFavoriteVideo(favoriteVideo: FavoriteVideo): Flowable<FavoriteVideo>

    fun loadFavoriteVideo(): Maybe<List<FavoriteVideo>>

    fun updateFavoriteVideo(favoriteVideo: FavoriteVideo): Flowable<FavoriteVideo>

    fun deleteFavoriteVideo(id: String): Flowable<String>

    fun existFavoriteVideo(id: String): Flowable<Boolean>
}