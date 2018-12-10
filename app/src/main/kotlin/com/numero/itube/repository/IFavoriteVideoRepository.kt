package com.numero.itube.repository

import com.numero.itube.repository.db.FavoriteVideo
import io.reactivex.Flowable
import io.reactivex.Maybe

interface IFavoriteVideoRepository {
    suspend fun createFavoriteVideo(favoriteVideo: FavoriteVideo)

    suspend fun loadFavoriteVideo(): List<FavoriteVideo>

    suspend fun updateFavoriteVideo(favoriteVideo: FavoriteVideo)

    suspend fun deleteFavoriteVideo(id: String)

    suspend fun existFavoriteVideo(id: String): Boolean
}