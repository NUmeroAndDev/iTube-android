package com.numero.itube.repository

import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.repository.db.FavoriteVideoDao
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

class FavoriteVideoRepository(private val favoriteVideoDao: FavoriteVideoDao) : IFavoriteVideoRepository {

    override fun createFavoriteVideo(favoriteVideo: FavoriteVideo): Flowable<FavoriteVideo> {
        return Flowable.just(favoriteVideo)
                .doOnNext {
                    favoriteVideoDao.create(favoriteVideo)
                }
                .subscribeOn(Schedulers.io())
    }

    override fun loadFavoriteVideo(): Maybe<List<FavoriteVideo>> {
        return favoriteVideoDao.findAll()
                .subscribeOn(Schedulers.io())
    }

    override fun updateFavoriteVideo(favoriteVideo: FavoriteVideo): Flowable<FavoriteVideo> {
        return Flowable.just(favoriteVideo)
                .doOnNext {
                    favoriteVideoDao.update(favoriteVideo)
                }
                .subscribeOn(Schedulers.io())
    }

    override fun deleteFavoriteVideo(id: String): Flowable<String> {
        return Flowable.just(id)
                .doOnNext {
                    favoriteVideoDao.deleteVideo(id)
                }
                .subscribeOn(Schedulers.io())
    }

    override fun existFavoriteVideo(id: String): Flowable<Boolean> {
        return Flowable.just(id)
                .flatMap {
                    favoriteVideoDao.findVideo(id)
                }
                .map {
                    it.isNotEmpty()
                }
                .subscribeOn(Schedulers.io())
    }

}