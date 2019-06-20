package com.numero.itube.repository

import com.numero.itube.model.FavoriteVideoList
import com.numero.itube.repository.db.FavoriteVideo

interface IFavoriteVideoRepository {
    suspend fun createFavoriteVideo(favoriteVideo: FavoriteVideo)

    suspend fun loadFavoriteVideo(): FavoriteVideoList

    suspend fun updateFavoriteVideo(favoriteVideo: FavoriteVideo)

    suspend fun deleteFavoriteVideo(id: String)

    suspend fun existFavoriteVideo(id: String): Boolean
}