package com.numero.itube.repository

import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.coroutines.experimental.Deferred

interface IFavoriteVideoRepository {
    fun createFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<FavoriteVideo>

    fun loadFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<List<FavoriteVideo>>

    fun updateFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<FavoriteVideo>

    fun deleteFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<FavoriteVideo>
}