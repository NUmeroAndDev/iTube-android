package com.numero.itube.repository

import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.repository.db.FavoriteVideoDao
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

class FavoriteVideoRepository(private val favoriteVideoDao: FavoriteVideoDao) : IFavoriteVideoRepository {

    override fun createFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<FavoriteVideo> = async {
        favoriteVideoDao.create(favoriteVideo)
        favoriteVideo
    }

    override fun loadFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<List<FavoriteVideo>> = async {
        favoriteVideoDao.findAll()
    }

    override fun updateFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<FavoriteVideo> = async {
        favoriteVideoDao.update(favoriteVideo)
        favoriteVideo
    }

    override fun deleteFavoriteVideo(favoriteVideo: FavoriteVideo): Deferred<FavoriteVideo> = async {
        favoriteVideoDao.delete(favoriteVideo)
        favoriteVideo
    }

}