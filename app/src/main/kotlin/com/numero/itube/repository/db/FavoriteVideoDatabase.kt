package com.numero.itube.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(FavoriteVideo::class)], version = 1)
abstract class FavoriteVideoDatabase : RoomDatabase() {

    abstract fun favoriteVideoDao(): FavoriteVideoDao

}