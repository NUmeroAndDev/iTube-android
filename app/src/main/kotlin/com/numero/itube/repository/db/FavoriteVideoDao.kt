package com.numero.itube.repository.db

import android.arch.persistence.room.*

@Dao
interface FavoriteVideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(favoriteVideo: FavoriteVideo)

    @Query("SELECT * FROM FavoriteVideo")
    fun findAll(): List<FavoriteVideo>

    @Query("SELECT * FROM FavoriteVideo WHERE id = :id")
    fun findVideo(id: String): List<FavoriteVideo>

    @Query("DELETE FROM FavoriteVideo WHERE id = :id")
    fun deleteVideo(id: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(favoriteVideo: FavoriteVideo)

    @Delete
    fun delete(favoriteVideo: FavoriteVideo)
}