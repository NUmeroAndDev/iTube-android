package com.numero.itube.repository

import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.coroutines.experimental.Deferred

interface IFavoriteVideoRepository {
    fun createFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<FavoriteVideo>
}