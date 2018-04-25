package com.numero.itube.repository.db

import android.arch.persistence.room.*

@Dao
interface FavoriteVideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createFavoriteVideo(favoriteVideo: FavoriteVideo)

    @Query("SELECT * FROM FavoriteVideo")
    fun findAll(): List<FavoriteVideo>

    @Update
    fun updateFavoriteVideo(favoriteVideo: FavoriteVideo)

    @Delete
    fun delete(favoriteVideo: FavoriteVideo)
}