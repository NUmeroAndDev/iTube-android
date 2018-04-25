package com.numero.itube.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(FavoriteVideo::class)], version = 1)
abstract class FavoriteVideoDatabase : RoomDatabase() {

    abstract fun favoriteVideoDao(): FavoriteVideoDao

}