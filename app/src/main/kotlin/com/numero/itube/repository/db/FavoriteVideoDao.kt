package com.numero.itube.repository.db

import android.arch.persistence.room.*

@Dao
interface FavoriteVideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(favoriteVideo: FavoriteVideo)

    @Query("SELECT * FROM FavoriteVideo")
    fun findAll(): List<FavoriteVideo>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(favoriteVideo: FavoriteVideo)

    @Delete
    fun delete(favoriteVideo: FavoriteVideo)
}