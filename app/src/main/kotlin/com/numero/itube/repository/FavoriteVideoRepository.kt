package com.numero.itube.repository

import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.repository.db.FavoriteVideoDao

class FavoriteVideoRepository(private val favoriteVideoDao: FavoriteVideoDao) : IFavoriteVideoRepository {

    override suspend fun createFavoriteVideo(favoriteVideo: FavoriteVideo) {
        favoriteVideoDao.create(favoriteVideo)
    }

    override suspend fun loadFavoriteVideo(): List<FavoriteVideo> {
        return favoriteVideoDao.findAll()
    }

    override suspend fun updateFavoriteVideo(favoriteVideo: FavoriteVideo) {
        return favoriteVideoDao.update(favoriteVideo)
    }

    override suspend fun deleteFavoriteVideo(id: String) {
        return favoriteVideoDao.deleteVideo(id)
    }

    override suspend fun existFavoriteVideo(id: String): Boolean {
        return favoriteVideoDao.findVideo(id).isNotEmpty()
    }

}