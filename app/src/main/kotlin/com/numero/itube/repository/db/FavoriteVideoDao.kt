package com.numero.itube.repository.db

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface FavoriteVideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(favoriteVideo: FavoriteVideo)

    @Query("SELECT * FROM FavoriteVideo")
    fun findAll(): Maybe<List<FavoriteVideo>>

    @Query("SELECT * FROM FavoriteVideo WHERE id = :id")
    fun findVideo(id: String): Flowable<List<FavoriteVideo>>

    @Query("DELETE FROM FavoriteVideo WHERE id = :id")
    fun deleteVideo(id: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(favoriteVideo: FavoriteVideo)

    @Delete
    fun delete(favoriteVideo: FavoriteVideo)
}